package org.redsxi.transitplus.common.network

import net.minecraft.client.player.LocalPlayer
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Player
import org.redsxi.transitplus.client.network.LinkClient
import org.redsxi.transitplus.server.network.LinkServer

interface Link {
    fun init()
    fun listen(type: PacketType, listener: suspend (Packet) -> Unit)
    fun send(packet: Packet)

    companion object {
        fun Player?.link(): Link {
            return when (this) {
                is ServerPlayer -> {
                    LinkServer.link(this)
                }
                is LocalPlayer -> {
                    LinkClient
                }
                else -> if (this == null) {
                    LinkServer.global()
                } else error("Invalid player")
            }
        }
    }
}