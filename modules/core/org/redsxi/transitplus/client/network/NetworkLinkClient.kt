package org.redsxi.transitplus.client.network

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.minecraft.resources.ResourceLocation
import org.redsxi.transitplus.common.network.EmptyPacket
import org.redsxi.transitplus.common.network.Packet
import org.redsxi.transitplus.common.network.PacketType

object NetworkLinkClient {
    fun registerPacketListener(type: PacketType, listener: (Packet) -> Unit) =
    ClientPlayNetworking.registerGlobalReceiver(type.id) { client, l, buf, sender ->
        val packet = type.create()
        val data = buf.readAnySizeNbt() ?: return@registerGlobalReceiver
        packet.loadData(data)
        listener(packet)
    }

    fun init() {
        // Nothing to initialize
    }

    fun sendPacket(pack: Packet) {
        val data = pack.getData()
        val buf = PacketByteBufs.create()
        buf.writeNbt(data)

        ClientPlayNetworking.send(pack.id, buf)
    }

    fun initCurrentConnection() = sendPacket(EmptyPacket)
}