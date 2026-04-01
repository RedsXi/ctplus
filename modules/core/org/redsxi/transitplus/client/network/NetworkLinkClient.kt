package org.redsxi.transitplus.client.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.io.IOException
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.minecraft.nbt.CompoundTag
import org.redsxi.transitplus.common.network.EmptyPacket
import org.redsxi.transitplus.common.network.Packet
import org.redsxi.transitplus.common.network.PacketType
import org.redsxi.transitplus.common.network.Request
import org.redsxi.transitplus.common.network.Response

object NetworkLinkClient {
    var requestNum = 0L

    val waitingRequest = HashMap<Long, Long>()
    val savedResponse = HashMap<Long, Response>()

    fun registerPacketListener(type: PacketType, listener: (Packet) -> Unit) =
    ClientPlayNetworking.registerGlobalReceiver(type.id) { client, l, buf, sender ->
        val packet = type.create()
        val data = buf.readAnySizeNbt() ?: return@registerGlobalReceiver
        packet.loadData(data)
        listener(packet)
    }

    fun init() {
        registerPacketListener(Response.Type) {
            if (it !is Response) return@registerPacketListener
            savedResponse[it.reqId] = it
        }
    }

    fun sendPacket(pack: Packet) {
        val data = pack.getData()
        val buf = PacketByteBufs.create()
        buf.writeNbt(data)

        ClientPlayNetworking.send(pack.id, buf)
    }

    fun initCurrentConnection() = sendPacket(EmptyPacket)

    suspend fun request(path: String, body: CompoundTag, timeout: Long = 10000L): CompoundTag = withContext(Dispatchers.IO) {
        val id = requestNum++
        val reqTime = System.currentTimeMillis()
        waitingRequest[id] = reqTime
        val reqPacket = Request(path, body, id)
        sendPacket(reqPacket)
        while(!savedResponse.containsKey(id)) {
            if( reqTime + timeout < System.currentTimeMillis()) throw IOException("Request timed out")
        }
        val response = savedResponse[id] ?: throw InternalError("Didn't find response packet but stopped hanging")
        savedResponse.remove(id)
        response.responseBody
    }
}