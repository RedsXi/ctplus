package org.redsxi.transitplus.server.network

import net.minecraft.nbt.NbtOps
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.Level
import org.redsxi.transitplus.common.data.ChunkPos
import org.redsxi.transitplus.common.data.rail.ChunkRail
import org.redsxi.transitplus.common.data.rail.Rail
import org.redsxi.transitplus.common.network.Network
import org.redsxi.transitplus.server.core.RailwaySystem.Companion.railwaySystem

object NetworkServer: Network {
    override fun init() {
        NetworkLinkServer.registerRequestProcessor("RCP.GetChunkRail") { tag, player ->
            ChunkRail.CODEC.encodeStart(NbtOps.INSTANCE, getChunkRail(ChunkPos.CODEC.decode(NbtOps.INSTANCE, tag).result().get().first, player.level)).result().get()
        }
    }

    override suspend fun getChunkRail(chunk: ChunkPos, world: Level): ChunkRail {
        val rails = (world as ServerLevel).railwaySystem().rails(ChunkPos(0, 0))

        return rails
    }
}