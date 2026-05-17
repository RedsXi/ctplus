package org.redsxi.transitplus.client.render.rail

import kotlinx.coroutines.withContext
import org.redsxi.transitplus.client.render.Vertexes
import org.redsxi.transitplus.common.data.rail.ArcRail
import org.redsxi.transitplus.common.data.rail.ChunkRail
import org.redsxi.transitplus.common.data.rail.RailSegment
import org.redsxi.transitplus.common.data.rail.SegmentRail
import org.redsxi.transitplus.common.data.rail.SpecialSegmentRail
import org.redsxi.transitplus.common.logger.logger
import org.redsxi.transitplus.coroutines.Dispatchers
import java.lang.StrictMath.*
import java.util.concurrent.*
import kotlin.math.pow

class RailRenderer(val rail: ChunkRail, val scale: Int): Callable<List<Vertexes>> { // scale == 0 is default
    override fun call(): List<Vertexes> {
        val result = ArrayList<Vertexes>()
        for (r in rail.rails.values) {
            result += getVertexes(r.start, 4.0)
            result += getVertexes(r.end, 4.0)
        }
        return result
    }

    /*
    private static Vec3 getPositionXZ(double h, double k, double r, double t, double radiusOffset, boolean isStraight) {
        return !isStraight ?
        new Vec3(
        h + (r + radiusOffset) * Math.cos(t / r) + (double)0.5F,
        (double)0.0F,
        k + (r + radiusOffset) * Math.sin(t / r) + (double)0.5F) : new Vec3(h * t + k * ((Math.abs(h) >= (double)0.5F && Math.abs(k) >= (double)0.5F ? (double)0.0F : r) + radiusOffset) + (double)0.5F, (double)0.0F, k * t + h * (r - radiusOffset) + (double)0.5F);
    }
     */

    fun getVertexes(r: RailSegment, width: Double): Vertexes {
        val result = Vertexes()
        val wh = width / 2
        val actualStroke = wh * SCALE_BASE.pow(-scale)
        if (r is SegmentRail) {
            line(
                r.kX,
                r.kY,
                r.kO,
                r.tStart,
                r.tEnd,
                actualStroke,
                false
            )
            { a, b -> result += Pair(a.toFloat(), b.toFloat()) }
        }
        if (r is SpecialSegmentRail) {
            line(
                r.kX,
                r.kY,
                r.kO,
                r.tStart,
                r.tEnd,
                actualStroke,
                true
            )
            { a, b -> result += Pair(a.toFloat(), b.toFloat()) }
        }
        if (r is ArcRail) {
            arc(
                r.cX,
                r.cY,
                r.r,
                r.tStart,
                r.tEnd,
                r.reverse,
                actualStroke
            )
            { a, b -> result += Pair(a.toFloat(), b.toFloat()) }
        }
        return result
    }

    fun posLine(
        kX: Double,
        kY: Double,
        kO: Double,
        t: Double,
        w: Double,
        special: Boolean,
    ): Pair<Double, Double> {
        val kXT = kX * t
        val kYT = kY * t
        val bX = kY * ((if (special) 0.0 else kO) + w)
        val bY = kX * (kO - w)
        return Pair(
            kXT + bX + 0.5,
            kYT + bY + 0.5
        )
    }

    fun line(
        kX: Double,
        kY: Double,
        kO: Double,
        tS: Double,
        tE: Double,
        wHalf: Double,
        special: Boolean,
        consumer: (Double, Double) -> Unit
    ) {
        val c: (Pair<Double, Double>) -> Unit = {
            consumer(it.first, it.second)
        }

        c(posLine(kX, kY, kO, tS, wHalf, special))
        c(posLine(kX, kY, kO, tS, -wHalf, special))
        c(posLine(kX, kY, kO, tE, wHalf, special))
        c(posLine(kX, kY, kO, tE, -wHalf, special))
    }

    fun arc(
        centerX: Double,
        centerY: Double,
        r: Double,
        tS: Double, // Both rad
        tE: Double,
        reverse: Boolean,
        strokeHalf: Double,
        consumer: (Double, Double) -> Unit // TRI_STRIP
    ) {

        val tStart = tS / r
        val tEnd = tE / r

        val d = abs(tEnd - tStart)
        val count = r.toInt() * RENDER_LEVEL
        val step = d / count

        val k = if (reverse) -1 else 1

        for(i in 0..count) {
            val theta = tStart + (step * i) * k
            val kX = cos(theta)
            val kY = sin(theta)
            val out = r + strokeHalf
            val inn = r - strokeHalf
            if (tEnd > tStart) {
                consumer(
                    centerX + out * kX + 0.5,
                    centerY + out * kY + 0.5
                )
                consumer(
                    centerX + inn * kX + 0.5,
                    centerY + inn * kY + 0.5
                )
            } else {
                consumer(
                    centerX + inn * kX + 0.5,
                    centerY + inn * kY + 0.5
                )
                consumer(
                    centerX + out * kX + 0.5,
                    centerY + out * kY + 0.5
                )
            }

        }
    }

    companion object {
        const val SCALE_BASE = 1.1
        const val RENDER_LEVEL = 8

        suspend fun render(rail: ChunkRail, scale: Int = 0): List<Vertexes> {
            return withContext(Dispatchers.RCP_RAIL_RENDERER) {
                RailRenderer(rail, scale).call()
            }
        }
    }
}