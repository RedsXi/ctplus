package org.redsxi.transitplus.server.network

import kotlinx.coroutines.runBlocking
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.LongTag
import net.minecraft.nbt.Tag
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerPlayer
import org.redsxi.mc.ctplus.generated.RuntimeVariables
import org.redsxi.transitplus.common.logger.logger
import org.redsxi.transitplus.common.network.EmptyPacket
import org.redsxi.transitplus.common.network.Packet
import org.redsxi.transitplus.common.network.PacketType
import org.redsxi.transitplus.common.network.Request
import org.redsxi.transitplus.common.network.Response

object NetworkLinkServer {
    val savedPlayerNetworkSession = HashMap<ServerPlayer, NetworkLinkServerSession>()

    fun ServerPlayer.networkLinkSession() = savedPlayerNetworkSession[this]

    fun registerPacketListener(type: PacketType, listener: (MinecraftServer, Packet, ServerPlayer) -> Unit) =
        ServerPlayNetworking.registerGlobalReceiver(type.id) { server, player, _, buf, _ ->

            val packet = type.create()
            val data = buf.readAnySizeNbt() ?: return@registerGlobalReceiver
            packet.loadData(data)
            if(RuntimeVariables.DEBUG) {
                val name = packet::class.java.simpleName
                logger.info("DEBUG: NL <- $name")
            }
            listener(server, packet, player)
        }

    fun init() {
        registerPacketListener(EmptyPacket.Type) { _, packet, player ->
            savedPlayerNetworkSession[player] = NetworkLinkServerSession(player)
        }
        registerPacketListener(Request.Type) { server, packet, player ->
            if(packet !is Request) return@registerPacketListener
            if(requestProcessor.containsKey(packet.reqPath))
                sendPacket(Response.createFromRequest(
                    packet,
                    runBlocking{(requestProcessor[packet.reqPath] ?: throw InternalError("Check log or make issue at GitHub"))(server, packet.requestBody, player) ?: CompoundTag()}
                ), player)
            else {
                sendPacket(Response.createFromRequest(packet, CompoundTag()), player)
            }
        }
        registerRequestProcessor("Ping") { _, _, _ -> LongTag.valueOf(System.currentTimeMillis()) }
    }

    fun sendPacket(pack: Packet, player: ServerPlayer) {
        val data = pack.getData()
        val buf = PacketByteBufs.create()
        buf.writeNbt(data)
        if(RuntimeVariables.DEBUG) {
            val name = pack::class.java.simpleName
            logger.warn("DEBUG: NL -> $name")
        }
        ServerPlayNetworking.send(player, pack.id, buf)
    }

    val requestProcessor = HashMap<String, suspend (MinecraftServer, Tag, ServerPlayer) -> Tag?>()

    fun registerRequestProcessor(path: String, processor: suspend (MinecraftServer, Tag, ServerPlayer) -> Tag?) {
        requestProcessor[path] = { server, tag, player ->
            val time = System.currentTimeMillis()
            if(RuntimeVariables.DEBUG) {
                logger.warn("DEBUG: NL-RRM <- REQ $path")
            }
            val result = processor(server, tag, player)
            if(RuntimeVariables.DEBUG) {
                logger.warn("DEBUG: NL-RRM -> RESP $path OK IN ${System.currentTimeMillis() - time}ms")
            }
            result
        }
    }
}