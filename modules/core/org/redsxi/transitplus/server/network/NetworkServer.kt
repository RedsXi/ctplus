package org.redsxi.transitplus.server.network

import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtOps
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.Level
import org.redsxi.transitplus.common.data.ChunkPos
import org.redsxi.transitplus.common.data.rail.ChunkRail
import org.redsxi.transitplus.common.network.Network
import org.redsxi.transitplus.server.core.RailwaySystem.Companion.railwaySystem

object NetworkServer: Network {
    override fun init() {
        NetworkLinkServer.registerRequestProcessor("RCP.GetChunkRail") { server, tag, player ->
            val req = tag as CompoundTag
            val chunkData = req.get("Chunk")
            val dimensionData = req.get("Dimension")
            val chunk = ChunkPos.CODEC.decode(NbtOps.INSTANCE, chunkData).result().get().first
            val dimension = server.getLevel(
                Level.RESOURCE_KEY_CODEC.decode(NbtOps.INSTANCE, dimensionData).result().get().first
            ) ?: throw InternalError()
            val chunkRail = getChunkRail(chunk, dimension)
            ChunkRail.CODEC.encodeStart(NbtOps.INSTANCE, chunkRail).result().get()
        }
    }

    override suspend fun getChunkRail(chunk: ChunkPos, world: Level): ChunkRail {
        val rails = (world as ServerLevel).railwaySystem().rails(ChunkPos(0, 0))
        return rails
    }


}