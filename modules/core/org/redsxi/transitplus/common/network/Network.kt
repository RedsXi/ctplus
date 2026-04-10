package org.redsxi.transitplus.common.network

import org.redsxi.transitplus.client.network.NetworkClient
import org.redsxi.transitplus.common.data.ChunkPos
import org.redsxi.transitplus.common.data.rail.ChunkRail
import org.redsxi.transitplus.server.network.NetworkServer

interface Network {
    fun init()

    suspend fun getChunkRail(chunk: ChunkPos): ChunkRail
}