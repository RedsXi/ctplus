package org.redsxi.transitplus.server.network

import net.minecraft.nbt.NbtOps
import org.redsxi.transitplus.common.data.ChunkPos
import org.redsxi.transitplus.common.data.rail.ChunkRail
import org.redsxi.transitplus.common.data.rail.Rail
import org.redsxi.transitplus.common.network.Network

object NetworkServer: Network {
    override fun init() {
        NetworkLinkServer.registerRequestProcessor("RCP.GetChunkRail") { tag, player ->
            ChunkRail.CODEC.encodeStart(NbtOps.INSTANCE, getChunkRail(ChunkPos.CODEC.decode(NbtOps.INSTANCE, tag).result().get().first)).result().get()
        }
    }

    override suspend fun getChunkRail(chunk: ChunkPos): ChunkRail {
        val rails = ChunkRail(chunk)
        val array = rails.rails as ArrayList<Rail>
        array += Rail.read(0.0,0.0,0.0,0.0,0.0,true,0.0,0.0,0.0,0.0,0.0,true)
        return rails
    }
}