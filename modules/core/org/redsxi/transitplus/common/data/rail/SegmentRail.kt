package org.redsxi.transitplus.common.data.rail

/**
 * 直线
 *
 * 绘制：
 *
 * x = kX*T + kY*kO
 * y = kY*T + kX*kO
 */
class SegmentRail(kX: Double, kY: Double, kO: Double, override val tStart: Double, override val tEnd: Double): RailSegment {
    override val h = kX
    override val k = kY
    override val r = kO

    override val straight = true
}