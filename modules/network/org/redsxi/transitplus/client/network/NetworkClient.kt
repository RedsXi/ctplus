package org.redsxi.transitplus.client.network

import kotlinx.coroutines.withContext
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtOps
import net.minecraft.world.level.Level
import org.redsxi.transitplus.common.data.ChunkPos
import org.redsxi.transitplus.common.data.rail.ChunkRail
import org.redsxi.transitplus.common.logger.logger
import org.redsxi.transitplus.common.network.Network
import org.redsxi.transitplus.coroutines.Dispatchers

object NetworkClient: Network {

    init {
        init()
    }

    override fun init() {
    }

    override suspend fun getChunkRail(chunk: ChunkPos, world: Level): ChunkRail = withContext(Dispatchers.NETWORK) {
        val req = CompoundTag()
        req.put("Chunk", ChunkPos.Companion.CODEC.encodeStart(NbtOps.INSTANCE, chunk).result().get())
        val worldName = Level.RESOURCE_KEY_CODEC.encodeStart(NbtOps.INSTANCE, world.dimension()).result().get()
        req.put("Dimension", worldName)
        val resp = CallClient.request("RCP.GetChunkRail", req)
        ChunkRail.Companion.CODEC.decode(NbtOps.INSTANCE, resp).result().get().first
    }
}