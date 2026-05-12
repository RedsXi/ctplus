package org.redsxi.transitplus.server.network

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.minecraft.nbt.Tag
import net.minecraft.server.level.ServerPlayer
import org.redsxi.transitplus.common.Instance
import org.redsxi.transitplus.common.getOrCreate
import org.redsxi.transitplus.common.logger.logger
import org.redsxi.transitplus.common.network.Call
import org.redsxi.transitplus.common.network.Link.Companion.link
import org.redsxi.transitplus.common.network.Request
import org.redsxi.transitplus.common.network.Response
import org.redsxi.transitplus.coroutines.Dispatchers
import java.util.concurrent.ConcurrentHashMap

class CallServer(val player: ServerPlayer?): Call {

    init {
        init()
    }

    val handlers = ConcurrentHashMap<String, suspend Call.(Instance, Tag?) -> Tag?>()
    private suspend fun processRequest(path: String, body: Tag?, instance: Instance): Tag? {
        if (!handlers.containsKey(path)) {
            return null
        }
        val handler = handlers[path]!!
        return withContext(Dispatchers.NETWORK) {
            this@CallServer.handler(instance, body)
        }
    }

    override fun init() {
        player.link().listen(Request.Type) { packet, instance, callback ->
            if (packet !is Request)
                return@listen
            CoroutineScope(Dispatchers.NETWORK).launch {
                callback(
                    Response.createFromRequest(
                        packet,
                        processRequest(packet.reqPath, packet.requestBody, instance)
                    )
                )
            }
        }
    }

    override suspend fun request(path: String, payload: Tag?, timeoutMillis: Long): Tag? = null

    override fun handle(path: String, handler: suspend Call.(Instance, Tag?) -> Tag?) {
        handlers[path] = handler
    }

    companion object {
        val savedLinks = ConcurrentHashMap<ServerPlayer?, Call>()
        fun call(player: ServerPlayer?) = savedLinks.getOrCreate(player, CallServer(player))
    }
}