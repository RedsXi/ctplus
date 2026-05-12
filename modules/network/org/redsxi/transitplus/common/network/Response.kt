package org.redsxi.transitplus.common.network

import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.Tag
import org.redsxi.mc.ctplus.idOf

class Response(var reqPath: String = "", var responseBody: Tag? = null, var reqId: Long = Request.INVALID_REQUEST): Packet() {
    companion object {
        val resp = idOf("network_response")
        fun createFromRequest(req: Request, body: Tag?): Response
            = Response(req.reqPath, body, req.reqId)
    }

    override val id = resp

    override fun loadData(tag: CompoundTag) {
        responseBody = tag.get("Body")
        reqId = tag.getLong("ReqId")
        reqPath = tag.getString("ReqPath")
    }

    override fun saveData(tag: CompoundTag) {
        responseBody?.let {
            tag.put("Body", it)
        }
        tag.putLong("ReqId", reqId)
        tag.putString("ReqPath", reqPath)
    }

    object Type: PacketType {
        override fun create() = Response()
        override val id = resp
    }
}