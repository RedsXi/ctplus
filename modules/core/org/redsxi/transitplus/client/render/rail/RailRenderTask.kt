package org.redsxi.transitplus.client.render.rail

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.redsxi.transitplus.common.data.rail.ArcRail
import org.redsxi.transitplus.common.data.rail.ChunkRail
import org.redsxi.transitplus.common.data.rail.RailSegment
import org.redsxi.transitplus.common.data.rail.SegmentRail
import org.redsxi.transitplus.common.data.rail.SpecialSegmentRail
import org.redsxi.transitplus.common.logger.logger
import java.awt.BasicStroke
import java.awt.Color
import java.awt.Shape
import java.awt.geom.Arc2D
import java.awt.geom.Line2D
import java.awt.image.BufferedImage
import java.awt.image.RenderedImage
import java.lang.StrictMath.*
import java.util.concurrent.*

typealias Vertexes = ArrayList<Pair<Float, Float>>

class RailRenderTask(val rail: ChunkRail, val scale: Int): Callable<List<Vertexes>> { // scale == 0 is default
    override fun call(): List<Vertexes> {
        val result = ArrayList<Vertexes>()
        for (r in rail.rails.values) {
            result += getVertexes(r.start, 4.0)
            result += getVertexes(r.end, 4.0)
        }
        return result
    }

    fun getVertexes(r: RailSegment, width: Double): Vertexes {
        val result = Vertexes()
        val wh = width / 2
        if (r is SegmentRail) {
            line(
                r.kX * r.tStart + r.kY + r.kO,
                r.kY * r.tStart + r.kX + r.kO,
                r.kX * r.tEnd + r.kY + r.kO,
                r.kY * r.tEnd + r.kX + r.kO,
                wh
            )
            { a, b -> result += Pair(a.toFloat(), b.toFloat()) }
        }
        if (r is SpecialSegmentRail) {
            line(
                r.kX * r.tStart,
                r.kY * r.tStart + r.kX + r.kO,
                r.kX * r.tEnd,
                r.kY * r.tEnd + r.kX + r.kO,
                wh
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
                wh
            )
            { a, b -> result += Pair(a.toFloat(), b.toFloat()) }
        }
        return result
    }

    fun line(
        startX: Double,
        startY: Double,
        endX: Double,
        endY: Double,
        strokeHalf: Double,
        consumer: (Double, Double) -> Unit // TRI_STRIP
    ) {
        val x = endX - startX
        val y = endY - startY
        val l = hypot(x, y)
        val k = strokeHalf / l
        val tX = x * k
        val tY = y * k
        consumer(startX + tY, startY - tX)
        consumer(startX - tY, startY + tX)
        consumer(endX - tY, startY + tX)
        consumer(endX + tY, startY - tX)
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
        // Limit t in [-pi, pi]
        val tStart = atan2(sin(tS), cos(tS))
        val tEnd = atan2(sin(tE), cos(tE))

        // get D,S,Step
        var d = tEnd - tStart
        if(d <= 0.0) d += PI * 2
        var s = tStart
        if(reverse) {
            s = tEnd
            d = PI * 2 - d
        }
        val count = r.toInt()
        val step = d / count

        for(i in 0..count) {
            val theta = s + step * i
            val kX = cos(theta)
            val kY = sin(theta)
            val out = r + strokeHalf
            val inn = r - strokeHalf
            consumer(
                centerX + out * kX,
                centerY + out * kY
            )
            consumer(
                centerX + inn * kX,
                centerY + inn * kY
            )
        }
    }

    @Deprecated("")
    fun toDegree(rad: Double)
        = atan2(sin(rad), cos(rad)) * R2D

    @Deprecated("")
    fun deltaDeg(start: Double, end: Double, reverse: Boolean): Double {
        val v = toDegree(end) - toDegree(start)
        return if(reverse) 360.0 + v else v
    }

    @Deprecated("")
    fun getShapes(rails: ChunkRail, s: Int, action: (Shape) -> Unit) {
        val scale = 1 shl s
        val mask = scale - 1
        val invMask = mask.inv()
        val x = rails.pos.x
        val y = rails.pos.y
        val posX = (x and invMask).toDouble()
        val posY = (y and invMask).toDouble()

        for(rail in rails.rails.values) {
            action(rail.start.getShape(posX, posY, scale))
            action(rail.end.getShape(posX, posY, scale))
        }
    }

    @Deprecated("")
    fun RailSegment.getShape(
        posX: Double,
        posY: Double,
        scale: Int
    ): Shape {
        if(this is ArcRail) {
            println(tStart)
            println(toDegree(tStart))
            println(deltaDeg(tStart, tEnd, reverse))
            return Arc2D.Double(
                (cX - r + posX) / scale * RENDER_LEVEL,
                (cY - r + posY) / scale * RENDER_LEVEL,
                (r * 2) / scale * RENDER_LEVEL,
                (r * 2) / scale * RENDER_LEVEL,
                toDegree(tStart),
                deltaDeg(tStart, tEnd, reverse),
                Arc2D.OPEN
            )
        }
        if(this is SegmentRail) {
            return Line2D.Double(
                (kX * tStart + kY * kO - posX) / scale * RENDER_LEVEL,
                (kY * tStart + kX * kO - posY) / scale * RENDER_LEVEL,
                (kX * tEnd + kY * kO - posX) / scale * RENDER_LEVEL,
                (kY * tEnd + kX * kO - posY) / scale * RENDER_LEVEL,
            )
        }
        if(this is SpecialSegmentRail) {
            return Line2D.Double(
                (kX * tStart - posX) / scale * RENDER_LEVEL,
                (kY * tStart + kX * kO - posY) / scale * RENDER_LEVEL,
                (kX * tEnd - posX) / scale * RENDER_LEVEL,
                (kY * tEnd + kX * kO - posY) / scale * RENDER_LEVEL,
            )
        }
        error("")
    }

    class Factory: ThreadFactory {
        @Volatile
        var number = 1

        override fun newThread(r: Runnable)
            = Thread(r, "RailRenderer#${number++}")

    }

    companion object {
        const val RENDER_LEVEL = 64
        const val CHUNK_SIZE = 16
        const val R2D = 180.0 / PI

        val threadPool = ThreadPoolExecutor(
            4,
            16,
            0,
            TimeUnit.SECONDS,
            LinkedBlockingQueue(),
            Factory()
        )

        suspend fun render(rail: ChunkRail, scale: Int = 0): List<Vertexes> {
            return withContext(Dispatchers.IO) {
                threadPool.submit(RailRenderTask(rail, scale)).get()
            }
        }

        fun stop() = threadPool.shutdown()
    }
}