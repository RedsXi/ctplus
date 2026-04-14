package org.redsxi.transitplus.common.data

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.BlockPos
import net.minecraft.core.SectionPos

data class ChunkPos(val x: Int, val y: Int) {
    companion object {
        val CODEC: Codec<ChunkPos> = RecordCodecBuilder.create { builder ->
            builder.group(
                Codec.INT.fieldOf("ChunkX").forGetter{it.x},
                Codec.INT.fieldOf("ChunkY").forGetter{it.y}
            ).apply(builder) { x, y ->
                ChunkPos(x, y)
            }
        }

        fun fromBlock(pos: BlockPos) = ChunkPos(
            SectionPos.blockToSectionCoord(pos.x),
            SectionPos.blockToSectionCoord(pos.z)
        )
    }
}