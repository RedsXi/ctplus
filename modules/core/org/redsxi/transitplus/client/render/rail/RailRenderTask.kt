package org.redsxi.transitplus.client.render.rail

import org.redsxi.transitplus.common.data.rail.ArcRail
import org.redsxi.transitplus.common.data.rail.ChunkRail
import org.redsxi.transitplus.common.logger.logger
import java.awt.Shape
import java.awt.geom.Arc2D
import java.awt.image.BufferedImage
import java.awt.image.RenderedImage
import java.util.concurrent.*
import kotlin.math.PI

class RailRenderTask(val rail: ChunkRail, val scale: Int): Callable<RenderedImage> { // scale == 0 is default
    override fun call(): RenderedImage {
        val image = BufferedImage(
            CHUNK_SIZE * RENDER_LEVEL,
            CHUNK_SIZE * RENDER_LEVEL,
            BufferedImage.TYPE_INT_ARGB
            )
        val graphics = image.createGraphics()
        getShapes(rail, scale) {
            graphics.draw(it)
        }
        logger.info("Rendered 1 Image")
        return image
    }

    fun rad2deg(rad: Double): Double {
        var result = rad * R2D
        while(result < -360.0) {
            result += 360.0
        }
        while(result >= 360.0) {
            result -= 360.0
        }
        return result
    }

    fun getShapes(rails: ChunkRail, s: Int, action: (Shape) -> Unit) {
        val scale = 1 shl s
        val mask = scale - 1
        val invMask = mask.inv()
        val x = rails.pos.x
        val y = rails.pos.y
        val posX = (x and invMask).toDouble()
        val posY = (y and invMask).toDouble()
        val tX = x and mask
        val tY = y and mask

        /**
         * 计算位置
         *
         *
         */

        for(rail in rails.rails.values) {
            if (rail.start is ArcRail) {
                val s = -rad2deg(rail.start.tStart)
                val e = -rad2deg(rail.start.tEnd)
                val shape = Arc2D.Double(
                    (rail.start.h - posX) * RENDER_LEVEL / scale,
                    (rail.start.k - posY) * RENDER_LEVEL / scale,
                    (rail.start.r * 2) * RENDER_LEVEL / scale,
                    (rail.start.r * 2) * RENDER_LEVEL / scale,
                    s,
                    e - s,
                    Arc2D.OPEN
                )
                action(shape)
            }
            if (rail.end is ArcRail) {
                val s = -rad2deg(rail.end.tStart)
                val e = -rad2deg(rail.end.tEnd)
                val shape = Arc2D.Double(
                    (rail.end.h - posX) * RENDER_LEVEL / scale,
                    (rail.end.k - posY) * RENDER_LEVEL / scale,
                    (rail.end.r * 2) * RENDER_LEVEL / scale,
                    (rail.end.r * 2) * RENDER_LEVEL / scale,
                    s,
                    e - s,
                    Arc2D.OPEN
                )
                action(shape)
            }
        }
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
            8,
            64,
            2,
            TimeUnit.SECONDS,
            LinkedBlockingDeque(),
            Factory()
        )

        fun render(rail: ChunkRail, scale: Int = 0): RenderedImage {
            return threadPool.submit(RailRenderTask(rail, scale)).get()
        }

        fun stop() = threadPool.shutdown()
    }
}