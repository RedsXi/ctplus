package org.redsxi.transitplus.client.ui

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.minecraft.core.SectionPos
import org.redsxi.mc.ctplus.mapping.Text
import org.redsxi.transitplus.client.network.NetworkClient
import org.redsxi.transitplus.client.render.RenderContext
import org.redsxi.transitplus.client.render.Vertexes
import org.redsxi.transitplus.client.render.rail.RailRenderer
import org.redsxi.transitplus.client.render.rail.RailRenderer.Companion.SCALE_BASE
import org.redsxi.transitplus.common.data.ChunkPos
import org.redsxi.transitplus.common.data.rail.ChunkRail
import org.redsxi.transitplus.coroutines.Dispatchers
import java.util.concurrent.ConcurrentHashMap
import kotlin.math.pow

// Fuck
class RcpScreen: IScreen(Text.translatable("ui", "rcp")) {

    val windowW get() = window.guiScaledWidth.toDouble()
    val windowH get() = window.guiScaledHeight.toDouble()

    val windowWHalf get() = windowW / 2
    val windowHHalf get() = windowH / 2

    var translateX = windowWHalf
    var translateY = windowHHalf

    var scale: Int = 0
        set(v) {
            if(v in -20..20) {
                field = v
            }
        }
    val sReal: Double get() = SCALE_BASE.pow(scale)

    override fun render(context: RenderContext, mouseX: Int, mouseY: Int) {
        renderBackground(context.stack)

        // Actual map render
        context.pushPose()

        context.scale(sReal, sReal)
        context.translate(translateX, translateY)

        /*
        [s^-1,   0, -wX]
        [   0,s^-1, -wY]
        [   0,   0,   1]
         */

        val startX = SectionPos.blockToSectionCoord(-translateX)
        val startY = SectionPos.blockToSectionCoord(-translateY)

        val endX = SectionPos.blockToSectionCoord(( windowW / sReal ) - translateX)
        val endY = SectionPos.blockToSectionCoord(( windowH / sReal ) - translateY)

        for(x in startX..endX) {
            for(y in startY..endY) {
                val pos = ChunkPos(x, y)
                val ves = getVertexes(pos, scale)
                ves?.forEach {
                    context.drawVertexes(it, white, RenderContext.DrawType.LINES_STRIP)
                }
            }
        }

        context.popPose()
    }

    override fun mouseScrolled(x: Double, y: Double, sV: Double): Boolean {
        val oldS = 1.1.pow(scale)
        scale += sV.toInt()
        val newS = 1.1.pow(scale)

        val delta = ( 1.0 / newS ) - ( 1.0 / oldS )

        translateX += delta * x
        translateY += delta * y

        return super.mouseScrolled(x, y, sV)
    }

    override fun mouseDragged(d: Double, e: Double, i: Int, f: Double, g: Double): Boolean {
        if (i == 0) {
            translateX += f / sReal
            translateY += g / sReal
        }
        return super.mouseDragged(d, e, i, f, g)
    }

    val bufferedImage = ConcurrentHashMap<Pair<ChunkPos, Int>, List<Vertexes>>()

    val cachedChunk = ConcurrentHashMap<ChunkPos, ChunkRail>()

    suspend fun getChunkRail(pos: ChunkPos): ChunkRail {
        val result = cachedChunk[pos] ?: withContext(Dispatchers.NETWORK) {
            val cr = NetworkClient.getChunkRail(pos, client.level ?: error(""))
            cachedChunk[pos] = cr
            cr
        }
        return result
    }

    fun getVertexes(pos: ChunkPos, scale: Int): List<Vertexes>? {
        val result = bufferedImage[Pair(pos, scale)]
        val key = Pair(pos, scale)
        if(result == null) {
            bufferedImage[key] = ArrayList()
            CoroutineScope(Dispatchers.RCP_RAIL_RENDERER).launch {
                try {
                    val rails = getChunkRail(pos)
                    bufferedImage[key] = RailRenderer.render(rails, scale)
                } catch (_: Exception) {
                    bufferedImage.remove(key)
                }
            }
        }
        return result
    }

}