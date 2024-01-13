package org.redsxi.mc.ctplus.core

import net.minecraft.core.BlockPos
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import org.redsxi.mc.ctplus.item.ItemCard
import org.redsxi.mc.ctplus.mapping.Text

/**
 * Yes. This, what you are seeing, is a new generation of mtr journey system. It has created a system based on cards.
 */
object TransitPlus {
    fun pass(item: ItemStack, player: Player, price: Int, position: BlockPos, currentLevel: Level, passSound: SoundEvent): Boolean {
        val compound = item.tag ?: return false
        if(item.item is ItemCard) {
            val card = (item.item as ItemCard).card

            card.loadData(compound)

            return if(card.isValid()) {
                if(card.pay(price)) {
                    player.displayClientMessage(card.getPassMessage(), true)
                    item.tag = card.saveData(compound)
                    currentLevel.playSound(player, position, passSound, SoundSource.BLOCKS)
                    true
                } else {
                    player.displayClientMessage(Text.gui("insufficient_balance", card.balance()), true)
                    false
                }
            } else {
                player.displayClientMessage(Text.gui("card_invalid"), true)
                false
            }
        } else {
            player.displayClientMessage(Text.gui("hold_card_to_pass"), true)
            return false
        }
    }
}