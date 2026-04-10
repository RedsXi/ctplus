package org.redsxi.transitplus.client.network

import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtOps
import org.redsxi.transitplus.common.data.ChunkPos
import org.redsxi.transitplus.common.data.rail.ChunkRail
import org.redsxi.transitplus.common.network.Network

object NetworkClient: Network {
    override fun init() {
    }

    override suspend fun getChunkRail(chunk: ChunkPos): ChunkRail {
        val req = ChunkPos.CODEC.encodeStart(NbtOps.INSTANCE, chunk).result().get() as CompoundTag
        val resp = NetworkLinkClient.request("RCP.GetChunkRail", req)
        return ChunkRail.CODEC.decode(NbtOps.INSTANCE, resp).result().get().first
    }

}