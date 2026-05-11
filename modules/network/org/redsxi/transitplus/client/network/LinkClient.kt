package org.redsxi.transitplus.client.network

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import org.redsxi.transitplus.common.network.Link
import org.redsxi.transitplus.common.network.Packet
import org.redsxi.transitplus.common.network.PacketType
import org.redsxi.transitplus.coroutines.Dispatchers

object LinkClient: Link {

    init {
        init()
    }

    override fun init() {
    }

    override fun listen(type: PacketType, listener: suspend (Packet) -> Unit) {
        ClientPlayNetworking.registerGlobalReceiver(type.id) { _, _, buf, _ ->
            val packet = Packet.read(
                type,
                buf.readAnySizeNbt() ?: return@registerGlobalReceiver
            )
            CoroutineScope(Dispatchers.NETWORK).launch{listener(packet)}
        }
    }

    override fun send(packet: Packet) {
        val data = packet.getData()
        val buf = PacketByteBufs.create()
        buf.writeNbt(data)
        ClientPlayNetworking.send(packet.id, buf)
    }
}