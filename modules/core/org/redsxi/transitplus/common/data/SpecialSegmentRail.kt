package org.redsxi.transitplus.common.data

/**
 * 当 k,r>=0.5 的直线
 *
 * 绘制：
 *
 * x = kX*T
 * y = kY*T + kX*kO
 */
class SpecialSegmentRail(kX: Double, kY: Double, kO: Double, override val tStart: Double, override val tEnd: Double): RailSegment {
    override val h = kX
    override val k = kY
    override val r = kO

    override val straight = true
}