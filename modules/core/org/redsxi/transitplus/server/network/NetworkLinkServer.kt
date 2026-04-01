package org.redsxi.transitplus.server.network

import kotlinx.coroutines.runBlocking
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.nbt.CompoundTag
import net.minecraft.server.level.ServerPlayer
import org.apache.logging.log4j.core.jmx.Server
import org.redsxi.transitplus.common.network.EmptyPacket
import org.redsxi.transitplus.common.network.Packet
import org.redsxi.transitplus.common.network.PacketType
import org.redsxi.transitplus.common.network.Request
import org.redsxi.transitplus.common.network.Response

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
        registerPacketListener(Request.Type) { packet, player ->
            if(packet !is Request) return@registerPacketListener
            if(requestProcessor.containsKey(packet.reqPath))
                sendPacket(Response.createFromRequest(packet, CompoundTag()), player)
            else {
                sendPacket(Response.createFromRequest(
                    packet,
                    runBlocking{(requestProcessor[packet.reqPath] ?: throw InternalError("Check log or make issue at GitHub"))(packet.requestBody, player) ?: CompoundTag()}
                ), player)
            }
        }
    }

    fun sendPacket(pack: Packet, player: ServerPlayer) {
        val data = pack.getData()
        val buf = PacketByteBufs.create()
        buf.writeNbt(data)

        ServerPlayNetworking.send(player, pack.id, buf)
    }

    val requestProcessor = HashMap<String, suspend (CompoundTag, ServerPlayer) -> CompoundTag?>()

    fun registerRequestProcessor(path: String, processor: suspend (CompoundTag, ServerPlayer) -> CompoundTag?) {
        requestProcessor[path] = processor
    }
}