package org.redsxi.transitplus.client.render.rail

import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.withContext
import org.redsxi.transitplus.common.data.rail.ArcRail
import org.redsxi.transitplus.common.data.rail.ChunkRail
import org.redsxi.transitplus.common.data.rail.RailSegment
import org.redsxi.transitplus.common.data.rail.SegmentRail
import org.redsxi.transitplus.common.data.rail.SpecialSegmentRail
import org.redsxi.transitplus.common.logger.logger
import org.redsxi.transitplus.coroutines.Dispatchers
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
        /*
         * // 1. 计算直线方向向量
    glm::vec2 dir = glm::vec2(endX - startX, endY - startY);
    float length = glm::length(dir);
    if (length < 0.0001f) return {}; // 避免零向量
    dir = glm::normalize(dir);

    // 2. 计算垂直方向（用于扩展线宽）
    glm::vec2 normal = glm::vec2(-dir.y, dir.x);
    glm::vec2 offset = normal * (strokeWidth / 2.0f);

    // 3. 生成矩形四个顶点（两个三角形）
    std::vector<glm::vec2> vertices = {
        // 三角形 1
        glm::vec2(startX) + offset,
        glm::vec2(startY) - offset,
        glm::vec2(endX) + offset,

        // 三角形 2
        glm::vec2(endX) + offset,
        glm::vec2(endY) - offset,
        glm::vec2(startY) - offset
    };
    return vertices;
         */


        val dX = endX - startX
        val dY = endY - startY
        val len = hypot(dX, dY)
        if (len <= 1e-4) return
        val x = dX / len
        val y = dY / len

        val oX = -y * strokeHalf
        val oY = x * strokeHalf

        consumer(startX - oX, startY - oY)
        consumer(startX + oX, startY + oY)
        consumer(endX - oX, endY - oY)
        consumer(endX + oX, endY + oY)

        // 1. startX + oX, startY + oY
        // 2. startX - oX, startY - oY
        // 3. endX + oX, endY + oY
        // 4. endX - oX, endY - oY


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
        var s = tStart
        if(d <= 0.0) {
            d = -d
        }
        if(reverse) {
            s = tEnd
            d = PI * 2 - d

        }
        val count = r.toInt() * RENDER_LEVEL
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

    class Factory: ThreadFactory {
        @Volatile
        var number = 1

        override fun newThread(r: Runnable)
            = Thread(r, "RailRenderer#${number++}")

    }

    companion object {
        const val RENDER_LEVEL = 8

        suspend fun render(rail: ChunkRail, scale: Int = 0): List<Vertexes> {
            return withContext(Dispatchers.RCP_RAIL_RENDERER) {
                RailRenderTask(rail, scale).call()
            }
        }
    }
}