package org.redsxi.transitplus.common.data.rail

class ArcRail(
    val cX: Double,
    val cY: Double,
    override val r: Double,
    override val tStart: Double,
    override val tEnd: Double,
    override val reverse: Boolean
): RailSegment {

    override val h = cX
    override val k = cY

    override val straight = false
}