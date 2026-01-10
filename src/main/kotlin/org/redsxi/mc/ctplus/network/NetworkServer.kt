package org.redsxi.mc.ctplus.network

import io.netty.buffer.Unpooled
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.server.level.ServerPlayer
import org.redsxi.mc.ctplus.openRailwayControlPanel

object NetworkServer {
    fun openDashboard(
        player: ServerPlayer
    ) {
        val buf = FriendlyByteBuf(Unpooled.buffer())
        ServerPlayNetworking.send(player, openRailwayControlPanel, buf)
    }
}