package org.redsxi.transitplus.common.data

interface RailSegment {
    val h: Double
    val k: Double
    val r: Double
    val tStart: Double
    val tEnd: Double

    val straight: Boolean

    companion object {
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