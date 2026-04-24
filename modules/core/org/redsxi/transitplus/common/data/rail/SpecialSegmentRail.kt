package org.redsxi.transitplus.common.data.rail

/**
 * 当 k,r>=0.5 的直线
 *
 * 绘制：
 *
 * x = kX*T
 * y = kY*T + kX*kO
 */
class SpecialSegmentRail(
    val kX: Double,
    val kY: Double,
    val kO: Double,
    override val tStart: Double,
    override val tEnd: Double,
    override val reverse: Boolean
): RailSegment {
    override val h = kX
    override val k = kY
    override val r = kO

    override val straight = true
}