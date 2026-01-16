package org.redsxi.mc.ctplus.item

import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import org.redsxi.mc.ctplus.network.NetworkServer

/**
 * Railway Dashboard
 */
class ItemRailwayControlPanel: Item(Properties().stacksTo(1)) {

    override fun use(
        level: Level,
        player: Player,
        interactionHand: InteractionHand
    ): InteractionResultHolder<ItemStack> {
        if(!level.isClientSide) NetworkServer.openDashboard(player as ServerPlayer)
        return super.use(level, player, interactionHand)
    }
}