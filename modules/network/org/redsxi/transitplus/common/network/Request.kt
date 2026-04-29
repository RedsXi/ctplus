package org.redsxi.transitplus.common.network

import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.Tag
import org.redsxi.mc.ctplus.idOf

open class Request(var reqPath: String = "", var requestBody: Tag = CompoundTag(), var reqId: Long = INVALID_REQUEST): Packet() {
    companion object {
        val req = idOf("network_request")
        const val INVALID_REQUEST = -1L
    }

    override val id = req

    override fun loadData(tag: CompoundTag) {
        requestBody = tag.get("Body") ?: CompoundTag()
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