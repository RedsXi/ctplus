package org.redsxi.transitplus.client.ui

import com.mojang.blaze3d.platform.NativeImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.minecraft.client.renderer.GameRenderer
import net.minecraft.client.renderer.texture.DynamicTexture
import net.minecraft.core.SectionPos
import net.minecraft.resources.ResourceLocation
import org.redsxi.mc.ctplus.mapping.Text
import org.redsxi.transitplus.client.network.NetworkClient
import org.redsxi.transitplus.client.render.RenderContext
import org.redsxi.transitplus.client.render.Temporary
import org.redsxi.transitplus.client.render.rail.RailRenderTask
import org.redsxi.transitplus.client.render.rail.Vertexes
import org.redsxi.transitplus.common.data.ChunkPos
import org.redsxi.transitplus.coroutines.Dispatchers
import java.io.ByteArrayInputStream
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
            if(v in -20..48) {
                field = v
            }
        }
    val sReal: Double get() = 1.1.pow(scale)

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
                val ves = getVertexes(pos)
                ves?.forEach {
                    context.drawVertexes(it, white)
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

    val bufferedImage = ConcurrentHashMap<ChunkPos, List<Vertexes>>()

    fun getVertexes(pos: ChunkPos): List<Vertexes>? {
        val result = bufferedImage[pos]
        if(result == null) {
            bufferedImage[pos] = ArrayList()
            CoroutineScope(Dispatchers.NETWORK).launch {
                try {
                    val rails = NetworkClient.getChunkRail(pos, client.level ?: error(""))
                    bufferedImage[pos] = RailRenderTask.render(rails)
                } catch (_: Exception) {
                    bufferedImage.remove(pos)
                }
            }
        }
        return result
    }

}