package org.redsxi.transitplus.common.network

import net.minecraft.world.level.Level
import org.redsxi.transitplus.common.data.ChunkPos
import org.redsxi.transitplus.common.data.rail.ChunkRail

interface Network {
    fun init()

    suspend fun getChunkRail(chunk: ChunkPos, world: Level): ChunkRail
}