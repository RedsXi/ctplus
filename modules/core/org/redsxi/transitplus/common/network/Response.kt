package org.redsxi.transitplus.common.network

import net.minecraft.nbt.CompoundTag
import org.redsxi.mc.ctplus.idOf

class Response(var reqPath: String = "", var responseBody: CompoundTag = CompoundTag(), var reqId: Long = Request.invalidRequest): Packet() {
    companion object {
        val resp = idOf("network_response")
        fun createFromRequest(req: Request, body: CompoundTag): Response
            = Response(req.reqPath, body, req.reqId)
    }

    override val id = resp

    override fun loadData(tag: CompoundTag) {
        responseBody = tag.getCompound("Body")
        reqId = tag.getLong("ReqId")
        reqPath = tag.getString("ReqPath")
    }

    override fun saveData(tag: CompoundTag) {
        tag.put("Body", responseBody)
        tag.putLong("ReqId", reqId)
        tag.putString("ReqPath", reqPath)
    }

    object Type: PacketType {
        override fun create() = Request()
        override val id = resp
    }
}