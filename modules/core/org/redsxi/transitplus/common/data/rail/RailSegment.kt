package org.redsxi.transitplus.common.data.rail

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder

interface RailSegment {
    val h: Double
    val k: Double
    val r: Double
    val tStart: Double
    val tEnd: Double

    val straight: Boolean

    companion object {
        val CODEC: Codec<RailSegment> = RecordCodecBuilder.create { builder ->
            builder.group(
                Codec.DOUBLE.fieldOf("h").forGetter{it.h},
                Codec.DOUBLE.fieldOf("k").forGetter{it.k},
                Codec.DOUBLE.fieldOf("r").forGetter{it.r},
                Codec.DOUBLE.fieldOf("tStart").forGetter{it.tStart},
                Codec.DOUBLE.fieldOf("tEnd").forGetter{it.tEnd},
                Codec.BOOL.fieldOf("straight").forGetter{it.straight}
            ).apply(builder){ h, k, r, tStart, tEnd, straight ->
                read(h, k, r, tStart, tEnd, straight)
            }
        }

        fun read(
            h: Double,
            k: Double,
            r: Double,
            tStart: Double,
            tEnd: Double,
            straight: Boolean
        ): RailSegment = if(straight) {
            if(k >= 0.5 && r >= 0.5) {
                SpecialSegmentRail(h, k, r, tStart, tEnd)
            } else {
                SegmentRail(h, k, r, tStart, tEnd)
            }
        } else {
            ArcRail(h, k, r, tStart, tEnd)
        }
    }
}