package org.redsxi.mc.capi

import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

class CardItem: Item(Properties()) {
    override fun use(
        level: Level?,
        player: Player?,
        interactionHand: InteractionHand?
    ): InteractionResultHolder<ItemStack?>? {
        return super.use(level, player, interactionHand)
    }
}