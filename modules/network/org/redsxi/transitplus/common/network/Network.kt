package org.redsxi.transitplus.common.network

import net.minecraft.client.player.LocalPlayer
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import org.redsxi.transitplus.client.network.NetworkClient
import org.redsxi.transitplus.common.data.ChunkPos
import org.redsxi.transitplus.common.data.rail.ChunkRail
import org.redsxi.transitplus.server.network.NetworkServer

interface Network {
    fun init()

    suspend fun getChunkRail(chunk: ChunkPos, world: Level): ChunkRail

    companion object {
        val global = NetworkServer(null)
        fun global() = global
        fun Player?.network(): Network {
            return when (this) {
                is ServerPlayer -> {
                    NetworkServer(this)
                }
                is LocalPlayer -> {
                    NetworkClient
                }
                else -> if (this == null) {
                    global
                } else error("Invalid player")
            }
        }
    }
}