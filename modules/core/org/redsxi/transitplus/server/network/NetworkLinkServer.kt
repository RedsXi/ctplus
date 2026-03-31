package org.redsxi.transitplus.server.network

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.server.level.ServerPlayer
import org.redsxi.transitplus.common.network.EmptyPacket
import org.redsxi.transitplus.common.network.Packet
import org.redsxi.transitplus.common.network.PacketType

object NetworkLinkServer {
    val savedPlayerNetworkSession = HashMap<ServerPlayer, NetworkLinkServerSession>()

    fun ServerPlayer.networkLinkSession() = savedPlayerNetworkSession[this]

    fun registerPacketListener(type: PacketType, listener: (Packet, ServerPlayer) -> Unit) =
        ServerPlayNetworking.registerGlobalReceiver(type.id) { server, player, l, buf, sender ->
            val packet = type.create()
            val data = buf.readAnySizeNbt() ?: return@registerGlobalReceiver
            packet.loadData(data)
            listener(packet, player)
        }

    fun init() {
        registerPacketListener(EmptyPacket.Type) { packet, player ->
            savedPlayerNetworkSession[player] = NetworkLinkServerSession(player)
        }
    }

    fun sendPacket(pack: Packet, player: ServerPlayer) {
        val data = pack.getData()
        val buf = PacketByteBufs.create()
        buf.writeNbt(data)

        ServerPlayNetworking.send(player, pack.id, buf)
    }
}