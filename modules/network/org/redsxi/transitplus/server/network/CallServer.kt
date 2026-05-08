package org.redsxi.transitplus.server.network

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.minecraft.nbt.Tag
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerPlayer
import org.redsxi.transitplus.client.network.LinkClient
import org.redsxi.transitplus.common.Instance
import org.redsxi.transitplus.common.getOrCreate
import org.redsxi.transitplus.common.network.Call
import org.redsxi.transitplus.common.network.Link
import org.redsxi.transitplus.common.network.Link.Companion.link
import org.redsxi.transitplus.common.network.Request
import org.redsxi.transitplus.common.network.Response
import org.redsxi.transitplus.coroutines.Dispatchers
import org.redsxi.transitplus.server.ServerInstance
import java.util.concurrent.ConcurrentHashMap

class CallServer private constructor(val player: ServerPlayer?): Call {
    val handlers = ConcurrentHashMap<String, suspend Call.(Instance, Tag?) -> Tag?>()
    private suspend fun processRequest(path: String, body: Tag?, instance: MinecraftServer? = null): Tag? {
        if (!handlers.containsKey(path)) {
            return null
        }
        val handler = handlers[path]!!
        return withContext(Dispatchers.NETWORK) {
            handler(ServerInstance(instance!!), body)
        }
    }

    override fun init() {
        player.link().listen(Request.Type) {
            if (it !is Request)
                return@listen
            CoroutineScope(Dispatchers.NETWORK).launch {
                LinkClient.send(
                    Response.createFromRequest(
                        it,
                        processRequest(it.reqPath, it.requestBody)
                    )
                )
            }
        }
    }

    override suspend fun request(path: String, payload: Tag?, timeoutMillis: Long): Tag?
        = processRequest(path, payload)

    override fun handle(path: String, handler: suspend Call.(Instance, Tag?) -> Tag?) {
        handlers[path] = handler
    }

    companion object {
        val savedLinks = ConcurrentHashMap<ServerPlayer?, Call>()
        fun call(player: ServerPlayer?) = savedLinks.getOrCreate(player, CallServer(player))
        fun global() = CallServer(null)
    }
}