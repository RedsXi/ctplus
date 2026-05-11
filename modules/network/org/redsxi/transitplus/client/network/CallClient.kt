package org.redsxi.transitplus.client.network

import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import net.minecraft.nbt.Tag
import org.redsxi.transitplus.common.Instance
import org.redsxi.transitplus.common.logger.logger
import org.redsxi.transitplus.common.network.Call
import org.redsxi.transitplus.common.network.Request
import org.redsxi.transitplus.common.network.Response
import org.redsxi.transitplus.coroutines.Dispatchers
import java.util.concurrent.ConcurrentHashMap

object CallClient: Call {
    init {
        init()
    }

    val cachedResponse = ConcurrentHashMap<Long, Response>()

    override fun init() {
        LinkClient.listen(Response.Type) {
            logger.info("$it")
            if (it !is Response)
                return@listen
            cachedResponse[it.reqId] = it
        }
    }

    override suspend fun request(path: String, payload: Tag?, timeoutMillis: Long): Tag? = withContext(Dispatchers.NETWORK) {
        val reqId = System.currentTimeMillis()
        val request = Request(path, payload, reqId)
        LinkClient.send(request)
        while(!cachedResponse.containsKey(reqId))
            if (System.currentTimeMillis() - reqId >= timeoutMillis)
                error("Request timeout")
            else
                delay(100)
        val response = cachedResponse[reqId]!!
        cachedResponse.remove(reqId)
        response.responseBody
    }

    override fun handle(path: String, handler: suspend Call.(Instance, Tag?) -> Tag?) = Unit
}