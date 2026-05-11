package org.redsxi.transitplus.common.network

import net.minecraft.client.player.LocalPlayer
import net.minecraft.nbt.Tag
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Player
import org.redsxi.transitplus.client.network.CallClient
import org.redsxi.transitplus.common.Instance
import org.redsxi.transitplus.server.network.CallServer

interface Call {
    fun init()
    suspend fun request(path: String, payload: Tag? = null, timeoutMillis: Long = 10000L): Tag?
    fun handle(path: String, handler: suspend Call.(Instance, Tag?) -> Tag?)

    companion object {
        fun Player?.call(): Call {
            return when (this) {
                is ServerPlayer -> {
                    CallServer(this)
                }
                is LocalPlayer -> {
                    CallClient
                }
                else -> if (this == null) {
                    CallServer(null)
                } else error("Invalid player")
            }
        }
    }
}