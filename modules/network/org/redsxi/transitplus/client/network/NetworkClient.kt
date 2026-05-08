package org.redsxi.transitplus.client.network

import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.withContext
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtOps
import net.minecraft.world.level.Level
import org.redsxi.transitplus.client.render.rail.RailRenderTask
import org.redsxi.transitplus.common.data.ChunkPos
import org.redsxi.transitplus.common.data.rail.ChunkRail
import org.redsxi.transitplus.common.network.Network
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

object NetworkClient: Network {
    override fun init() {
    }

    override suspend fun getChunkRail(chunk: ChunkPos, world: Level): ChunkRail {
        val req = CompoundTag()
        req.put("Chunk", ChunkPos.Companion.CODEC.encodeStart(NbtOps.INSTANCE, chunk).result().get())
        val worldName = Level.RESOURCE_KEY_CODEC.encodeStart(NbtOps.INSTANCE, world.dimension()).result().get()
        req.put("Dimension", worldName)
        val resp = CallClient.request("RCP.GetChunkRail", req)
        return ChunkRail.Companion.CODEC.decode(NbtOps.INSTANCE, resp).result().get().first
    }
}