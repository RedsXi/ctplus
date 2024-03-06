package org.redsxi.mc.ctplus.core

import net.minecraft.core.BlockPos
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import org.redsxi.bool.Bool
import org.redsxi.mc.ctplus.blockentity.BlockEntityTicketBarrierPayDirect
import org.redsxi.mc.ctplus.card.Card
import org.redsxi.mc.ctplus.core.TransitPlus.PassType.*
import org.redsxi.mc.ctplus.data.CardContext
import org.redsxi.mc.ctplus.data.CardData
import org.redsxi.mc.ctplus.item.ItemCard
import org.redsxi.mc.ctplus.mapping.Text
import org.redsxi.mc.ctplus.mapping.Text.GUI
import org.redsxi.mc.ctplus.util.MTROptionalData
import org.redsxi.mc.ctplus.util.MTRTranslation
import kotlin.math.abs


object TransitPlus {
    private const val PRICE_RADIX = 2

    private fun encodeZone(zone: Int): Float = Float.fromBits(zone)
    private fun decodeZone(zone: Float): Int = zone.toBits()

    private fun price(entryZone: Int, exitZone: Int): Int {
        return abs(entryZone - exitZone) * PRICE_RADIX
    }

    /*/**
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
    }*/

    enum class PassType {
        PAY_DIRECT,
        ENTRY,
        EXIT
    }

    private fun <CDT: CardData, CT: Card<CDT, CT>> pass(cardCtx: CardContext<CDT, CT>, price: Int, player: Player, passFunc: () -> Unit): Bool =
        if(cardCtx.card.pay(cardCtx.data, price)) {
            player.displayClientMessage(Text.translatable(GUI, "gui.cgcem.enter_barrier", price), true)
            passFunc()
            Bool.TRUE
        } else {
            player.displayClientMessage(Text.translatable(GUI, "insufficient_balance", cardCtx.card.balance(cardCtx.data)), true)
            Bool.FALSE
        }

    private fun <CDT: CardData, CT: Card<CDT, CT>> enter(cardCtx: CardContext<CDT, CT>, zone: Int, stationName: String, stationNameTranslated: String, player: Player, passFunc: () -> Unit): Bool {
        if(cardCtx.card.balance(cardCtx.data) < 0 && !cardCtx.card.canOverdraft(cardCtx.data)) {
            player.displayClientMessage(Text.translatable(GUI, "insufficient_balance", cardCtx.card.balance(cardCtx.data)), true)
            return Bool.FALSE
        }
        if(cardCtx.data.isEntered) {
            player.displayClientMessage(Text.translatable(GUI, "card_invalid"), true)
            return Bool.FALSE
        }
        cardCtx.data.entryZoneEncoded = encodeZone(zone)
        cardCtx.data.entryStationName = stationName
        cardCtx.data.isEntered = true
        player.displayClientMessage(Text.translatable(GUI, "entered_station", stationNameTranslated as Any, cardCtx.card.balance(cardCtx.data)), true)
        passFunc()
        return Bool.TRUE
    }

    private fun <CDT: CardData, CT: Card<CDT, CT>> exit(cardCtx: CardContext<CDT, CT>, zone: Int, stationNameTranslated: String, player: Player, passFunc: () -> Unit): Bool {
        if(!cardCtx.data.isEntered) {
            player.displayClientMessage(Text.translatable(GUI, "card_invalid"), true)
            return Bool.FALSE
        }
        val price = price(decodeZone(cardCtx.data.entryZoneEncoded), zone)
        return if(cardCtx.card.pay(cardCtx.data, price)) {
            cardCtx.data.isEntered = false
            player.displayClientMessage(Text.translatable(GUI, "exited_station", stationNameTranslated as Any, price, cardCtx.card.balance(cardCtx.data)), true)
            passFunc()
            Bool.TRUE
        } else {
            player.displayClientMessage(Text.translatable(GUI, "insufficient_balance", cardCtx.card.balance(cardCtx.data)), true)
            Bool.FALSE
        }
    }

    fun pass(player: Player, position: BlockPos, world: Level, passSound: SoundEvent, passType: PassType): Bool {
        val playSoundFunc = {
            world.playSound(player, position, passSound, SoundSource.BLOCKS)
        }
        val stack = player.mainHandItem
        if (stack == ItemStack.EMPTY) {
            player.displayClientMessage(Text.translatable(GUI, "hold_card_to_pass"), true)
            return Bool.FALSE
        }
        val item = stack.item
        if (item is ItemCard<*, *>) {
            val card = item.card
            val context = card.context(stack)

            if(context.isValid()) {
                val result = when (passType) {
                    PAY_DIRECT -> {
                        val bEntity = world.getBlockEntity(position)
                        if (bEntity is BlockEntityTicketBarrierPayDirect) {
                            val price = bEntity.price
                            pass(context, price, player, playSoundFunc)
                        } else Bool.FALSE // If I forgot to register the block entity
                    }
                    else -> {
                        val stationOptional = MTROptionalData.getStation(
                            MTROptionalData.getRailwayData(world),
                            position
                        )
                        if(!stationOptional.isPresent) {
                            player.displayClientMessage(Text.translatable(GUI, "barrier_not_inside_the_station"), true)
                            return Bool.FALSE
                        }
                        val station = stationOptional.get()
                        val zone = station.zone

                        when (passType) {
                            ENTRY -> {
                                enter(context, zone, station.name, MTRTranslation.getTranslation(station.name), player, playSoundFunc)
                                //Bool.FALSE
                            }
                            EXIT -> {
                                exit(context, zone, MTRTranslation.getTranslation(station.name), player, playSoundFunc)
                                //Bool.FALSE
                            }
                            else -> Bool.FALSE // Impossible but kotlin tell me to do this
                        }
                    }
                }
                context.update()
                return result
            } else {
                context.update()
                player.displayClientMessage(Text.translatable(GUI, "card_invalid"), true)
                return Bool.FALSE
            }
        } else {
            player.displayClientMessage(Text.translatable(GUI, "hold_card_to_pass"), true)
            return Bool.FALSE
        }
    }
}