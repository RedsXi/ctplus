package org.redsxi.transitplus.common.data.rail

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.BlockPos
import org.redsxi.transitplus.common.data.ChunkPos

class ChunkRail(val pos: ChunkPos, val rails: MutableMap<Pair<BlockPos, BlockPos>, Rail> = HashMap()) {
    companion object {
        val CODEC: Codec<ChunkRail> = RecordCodecBuilder.create { builder ->
            builder.group(
                ChunkPos.CODEC.fieldOf("ChunkPos").forGetter{it.pos},
                Rail.MAP_POSITIONED_RAIL_CODEC.fieldOf("Rails").forGetter{it.rails}
            ).apply(builder) { pos, rails ->
                ChunkRail(pos, rails)
            }
        }
    }
}