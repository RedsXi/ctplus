package org.redsxi.mc.ctplus.core

import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.Level
import org.redsxi.bool.Bool
import org.redsxi.bool.False
import org.redsxi.bool.True
import org.redsxi.mc.ctplus.blockentity.BlockEntityTicketBarrierPayDirect
import org.redsxi.mc.ctplus.card.Card
import org.redsxi.mc.ctplus.core.TransitPlus.PassType.*
import org.redsxi.mc.ctplus.item.ItemCard
import org.redsxi.mc.ctplus.mapping.Text
import org.redsxi.mc.ctplus.mapping.Text.GUI
import org.redsxi.mc.ctplus.util.MTROptionalData
import org.redsxi.mc.ctplus.util.MTRTranslation
import kotlin.math.abs

/**
 * Yes. This is a new generation of mtr journey system. It has created a system based on cards.
 */
object TransitPlus {
    private const val PRICE_RADIX = 2

    private fun encodeZone(zone: Int): Float = Float.fromBits(zone)
    private fun decodeZone(zone: Float): Int = zone.toBits()

    private fun price(entryZone: Int, exitZone: Int): Int {
        return abs(entryZone - exitZone) * PRICE_RADIX
    }

    /**
     * @see pass
     */
    @Deprecated("Use another one")
    fun pass(player: Player, price: Int, position: BlockPos, currentLevel: Level, passSound: SoundEvent): Boolean {
        val itemStack = player.mainHandItem
        if(itemStack == ItemStack.EMPTY) {
            player.displayClientMessage(Text.translatable(GUI, "hold_card_to_pass"), true)
            return false
        }
        val item = itemStack.item
        if (item is ItemCard) {
            val card = item.card
            var compound = itemStack.tag
            if(compound == null) {
                compound = card.createData()
            }
            card.loadData(compound)
            return if(card.isValid()) {
                if(card.pay(price)) {
                    player.displayClientMessage(card.getPassMessage(), true)
                    itemStack.tag = card.createData()
                    currentLevel.playSound(player, position, passSound, SoundSource.BLOCKS)
                    true
                } else {
                    player.displayClientMessage(Text.translatable(GUI, "insufficient_balance", card.balance()), true)
                    false
                }
            } else {
                player.displayClientMessage(Text.translatable(GUI, "card_invalid"), true)
                false
            }
        } else {
            player.displayClientMessage(Text.translatable(GUI, "hold_card_to_pass"), true)
            return false
        }
    }

    enum class PassType {
        PAY_DIRECT,
        ENTRY,
        EXIT
    }

    private fun pass(card: Card, price: Int, player: Player, passFunc: () -> Unit): Bool =
        if(card.pay(price)) {
            player.displayClientMessage(card.getPassMessage(), true)
            passFunc()
            True
        } else {
            player.displayClientMessage(Text.translatable(GUI, "insufficient_balance", card.balance()), true)
            False
        }

    private fun enter(card: Card, zone: Int, stationName: String, stationNameTranslated: String, player: Player, passFunc: () -> Unit): Bool {
        if(card.isEntered.getK()) {
            player.displayClientMessage(Text.translatable(GUI, "card_invalid"), true)
            return False
        }
        card.entryZoneEncoded = encodeZone(zone)
        card.entryStationName = stationName
        card.isEntered = Bool.fromK(true)
        player.displayClientMessage(Text.translatable(GUI, "entered_station", stationNameTranslated, card.balance()), true)
        passFunc()
        return True
    }

    private fun exit(card: Card, zone: Int, stationNameTranslated: String, player: Player, passFunc: () -> Unit): Bool {
        if(!card.isEntered.getK()) {
            player.displayClientMessage(Text.translatable(GUI, "card_invalid"), true)
            return False
        }
        val price = price(decodeZone(card.entryZoneEncoded), zone)
        return if(card.pay(price)) {
            card.isEntered = Bool.fromK(false)
            player.displayClientMessage(Text.translatable(GUI, "exited_station", stationNameTranslated, card.balance()), true)
            passFunc()
            True
        } else {
            player.displayClientMessage(Text.translatable(GUI, "insufficient_balance", card.balance()), true)
            False
        }
    }

    fun pass(player: Player, position: BlockPos, world: Level, passSound: SoundEvent, passType: PassType): Bool {
        val playSoundFunc = {
            world.playSound(player, position, passSound, SoundSource.BLOCKS)
        }
        val stack = player.mainHandItem
        if (stack == ItemStack.EMPTY) {
            player.displayClientMessage(Text.translatable(GUI, "hold_card_to_pass"), true)
            return False
        }
        val item = stack.item
        if (item is ItemCard) {
            val card = item.card
            var data = stack.tag
            if (data == null) {
                data = card.createData()
            }
            card.loadData(data)
            if(card.isValid()) {
                val result = when (passType) {
                    PAY_DIRECT -> {
                        val bEntity = world.getBlockEntity(position)
                        if (bEntity is BlockEntityTicketBarrierPayDirect) {
                            val price = bEntity.price
                            pass(card, price, player, playSoundFunc)
                        } else False // If I forgot to register the block entity
                    }
                    else -> {
                        val stationOptional = MTROptionalData.getStation(
                            MTROptionalData.getRailwayData(world),
                            position
                        )
                        if(!stationOptional.isPresent) {
                            player.displayClientMessage(Text.translatable(GUI, "barrier_not_inside_the_station"), true)
                            return False
                        }
                        val station = stationOptional.get()
                        val zone = station.zone

                        when (passType) {
                            ENTRY -> {
                                enter(card, zone, station.name, MTRTranslation.getTranslation(world, station.name), player, playSoundFunc)
                                //False
                            }
                            EXIT -> {
                                exit(card, zone, MTRTranslation.getTranslation(world, station.name), player, playSoundFunc)
                                //False
                            }
                            else -> False // Impossible but kotlin tell me to do this
                        }
                    }
                }
                stack.tag = card.createData()
                return result
            } else {
                player.displayClientMessage(Text.translatable(GUI, "card_invalid"), true)
                return False
            }
        } else {
            player.displayClientMessage(Text.translatable(GUI, "hold_card_to_pass"), true)
            return False
        }
    }
}