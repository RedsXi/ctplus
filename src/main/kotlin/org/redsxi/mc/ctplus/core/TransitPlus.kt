package org.redsxi.mc.ctplus.core

import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.InteractionHand
import net.minecraft.world.item.ItemStack
import org.redsxi.mc.ctplus.item.ItemCard

/**
 * Yes. This, what you are seeing, is a new generation of mtr journey system. It has created a system based on cards.
 */
object TransitPlus {
    fun test(player: ServerPlayer) {

        player.getItemInHand(InteractionHand.MAIN_HAND)
    }

    fun pass(item: ItemStack, player: ServerPlayer, price: Int) {
        val compound = item.tag
        if(item.item is ItemCard) {
            val card = (item.item as ItemCard).card

        }
    }
}