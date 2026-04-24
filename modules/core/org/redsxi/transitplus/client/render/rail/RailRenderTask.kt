package org.redsxi.transitplus.client.render.rail

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

class RailRenderTask(val rail: ChunkRail, val scale: Int): Callable<RenderedImage> { // scale == 0 is default
    override fun call(): RenderedImage {
        val image = BufferedImage(
            CHUNK_SIZE * RENDER_LEVEL * 2,
            CHUNK_SIZE * RENDER_LEVEL * 2,
            BufferedImage.TYPE_INT_ARGB
            )
        val graphics = image.createGraphics()
        graphics.color = Color.YELLOW
        graphics.stroke = BasicStroke(RENDER_LEVEL.toFloat())
        getShapes(rail, scale) {
            graphics.draw(it)
        }
        logger.info("Rendered 1 Image")
        return image
    }


    fun toDegree(rad: Double)
        = atan2(sin(rad), cos(rad)) * R2D

    fun deltaDeg(start: Double, end: Double, reverse: Boolean): Double {
        val v = toDegree(end) - toDegree(start)
        return if(reverse) 360.0 + v else v
    }

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
            LinkedBlockingDeque(),
            Factory()
        )

        suspend fun render(rail: ChunkRail, scale: Int = 0): RenderedImage {
            return threadPool.submit(RailRenderTask(rail, scale)).get()
        }

        fun stop() = threadPool.shutdown()
    }
}