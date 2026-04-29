package org.redsxi.transitplus.server.network

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.server.level.ServerPlayer
import org.redsxi.transitplus.common.network.EmptyPacket
import org.redsxi.transitplus.common.network.Link
import org.redsxi.transitplus.common.network.Packet
import org.redsxi.transitplus.common.network.PacketType

class LinkServer(val player: ServerPlayer): Link {
    override fun init() {
        listen(EmptyPacket.Type) {}
    }

    override fun listen(type: PacketType, listener: suspend (Packet) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun send(packet: Packet) {
        val data = packet.getData()
        val buf = PacketByteBufs.create()
        buf.writeNbt(data)
        ServerPlayNetworking.send(player, packet.id, buf)
    }
}