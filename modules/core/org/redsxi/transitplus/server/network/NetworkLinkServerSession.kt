package org.redsxi.transitplus.server.network

import net.minecraft.server.level.ServerPlayer
import org.redsxi.transitplus.common.network.Packet
import org.redsxi.transitplus.common.network.PacketType

class NetworkLinkServerSession(val player: ServerPlayer) {
    fun registerPacketListener(type: PacketType, listener: (Packet, ServerPlayer) -> Unit) = NetworkLinkServer.registerPacketListener(type) { packet, p ->
        if(p != player) return@registerPacketListener
        listener(packet, player)
    }

    fun sendPacket(pack: Packet) = NetworkLinkServer.sendPacket(pack, player)
}