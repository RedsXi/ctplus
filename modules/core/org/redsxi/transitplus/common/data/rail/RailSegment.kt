package org.redsxi.transitplus.common.data.rail

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import kotlin.math.abs

interface RailSegment {
    val h: Double
    val k: Double
    val r: Double
    val tStart: Double
    val tEnd: Double

    val straight: Boolean

    val reverse: Boolean

    companion object {
        val CODEC: Codec<RailSegment> = RecordCodecBuilder.create { builder ->
            builder.group(
                Codec.DOUBLE.fieldOf("H").forGetter{it.h},
                Codec.DOUBLE.fieldOf("K").forGetter{it.k},
                Codec.DOUBLE.fieldOf("R").forGetter{it.r},
                Codec.DOUBLE.fieldOf("Start").forGetter{it.tStart},
                Codec.DOUBLE.fieldOf("End").forGetter{it.tEnd},
                Codec.BOOL.fieldOf("Straight").forGetter{it.straight},
                Codec.BOOL.fieldOf("Reverse").forGetter{it.reverse},
            ).apply(builder){ h, k, r, tStart, tEnd, straight, reverse ->
                read(h, k, r, tStart, tEnd, straight, reverse)
            }
        }

        fun read(
            h: Double,
            k: Double,
            r: Double,
            tStart: Double,
            tEnd: Double,
            straight: Boolean,
            reverse: Boolean
        ): RailSegment = if(straight) {
            if(abs(h) >= 0.5 && abs(k) >= 0.5) {
                SpecialSegmentRail(h, k, r, tStart, tEnd, reverse)
            } else {
                SegmentRail(h, k, r, tStart, tEnd, reverse)
            }
        } else {
            ArcRail(h, k, r, tStart, tEnd, reverse)
        }
    }
}