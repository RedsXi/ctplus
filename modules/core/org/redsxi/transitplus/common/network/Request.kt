package org.redsxi.transitplus.common.network

import net.minecraft.nbt.CompoundTag
import net.minecraft.resources.ResourceLocation
import org.redsxi.mc.ctplus.idOf

class Request(var reqPath: String = "", var requestBody: CompoundTag = CompoundTag(), var reqId: Long = invalidRequest): Packet() {
    companion object {
        val req = idOf("network_request")
        const val invalidRequest = -1L
    }

    override val id = req

    override fun loadData(tag: CompoundTag) {
        requestBody = tag.getCompound("Body")
        reqId = tag.getLong("ReqId")
        reqPath = tag.getString("ReqPath")
    }

    override fun saveData(tag: CompoundTag) {
        tag.put("Body", requestBody)
        tag.putLong("ReqId", reqId)
        tag.putString("ReqPath", reqPath)
    }

    object Type: PacketType {
        override fun create() = Request()
        override val id = req
    }
}