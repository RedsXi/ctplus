package org.redsxi.mc.ctplus.core

import net.minecraft.core.BlockPos
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import org.redsxi.mc.ctplus.item.ItemCard
import org.redsxi.mc.ctplus.mapping.Text
import org.redsxi.mc.ctplus.mapping.Text.GUI

/**
 * Yes. This, what you are seeing, is a new generation of mtr journey system. It has created a system based on cards.
 */
object TransitPlus {
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
}