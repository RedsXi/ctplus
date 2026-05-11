package org.redsxi.transitplus.server.network

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.server.level.ServerPlayer
import org.redsxi.transitplus.common.getOrCreate
import org.redsxi.transitplus.common.network.EmptyPacket
import org.redsxi.transitplus.common.network.Link
import org.redsxi.transitplus.common.network.Packet
import org.redsxi.transitplus.common.network.PacketType
import org.redsxi.transitplus.coroutines.Dispatchers
import java.util.concurrent.ConcurrentHashMap

class LinkServer private constructor(val player: ServerPlayer?): Link {

    init {
        init()
    }

    override fun init() {
        listen(EmptyPacket.Type) {}
    }

    override fun listen(type: PacketType, listener: suspend (Packet) -> Unit) {
        ServerPlayNetworking.registerGlobalReceiver(type.id) { _, sender, _, buf, _->
            if(player == null || player == sender) {
                val packet = Packet.read(
                    type,
                    buf.readAnySizeNbt() ?: return@registerGlobalReceiver
                )
                CoroutineScope(Dispatchers.NETWORK).launch{listener(packet)}
            }
        }
    }

    override fun send(packet: Packet) {
        val data = packet.getData()
        val buf = PacketByteBufs.create()
        buf.writeNbt(data)
        ServerPlayNetworking.send(player, packet.id, buf)
    }

    companion object {
        val savedLinks = ConcurrentHashMap<ServerPlayer?, Link>()
        fun link(player: ServerPlayer?) = savedLinks.getOrCreate(player, LinkServer(player))
        val global = LinkServer(null)
        fun global() = global
    }
}