package org.redsxi.transitplus.common.data

import org.redsxi.transitplus.server.mixin.RailAccessor

class Rail(val start: RailSegment, end: RailSegment) {
    companion object {
        fun read(
            h1: Double,
            k1: Double,
            r1: Double,
            tStart1: Double,
            tEnd1: Double,
            straight1: Boolean,
            h2: Double,
            k2: Double,
            r2: Double,
            tStart2: Double,
            tEnd2: Double,
            straight2: Boolean
        ) = Rail(
            RailSegment.read(h1, k1, r1, tStart1, tEnd1, straight1),
            RailSegment.read(h2, k2, r2, tStart2, tEnd2, straight2),
        )

        /**
         * @param rail 假设r0是MTR的Rail: ```r0.accessor()```
         */
        fun read(rail: RailAccessor)
            = read(
                rail.hStart(),
                rail.kStart(),
                rail.rStart(),
                rail.tStartStart(),
                rail.tStartEnd(),
                rail.startStraight(),
                rail.hEnd(),
                rail.kEnd(),
                rail.rEnd(),
                rail.tEndStart(),
                rail.tEndEnd(),
                rail.endStraight()
            )
    }
}