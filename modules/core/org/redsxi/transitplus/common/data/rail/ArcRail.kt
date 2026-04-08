package org.redsxi.transitplus.common.data.rail

class ArcRail(cX: Double, cY: Double, override val r: Double, override val tStart: Double, override val tEnd: Double): RailSegment {
    override val h = cX
    override val k = cY

    override val straight = false
}