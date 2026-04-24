package org.redsxi.transitplus.client.ui

import com.mojang.blaze3d.platform.NativeImage
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiComponent
import net.minecraft.client.gui.components.Button
import net.minecraft.client.renderer.GameRenderer
import net.minecraft.client.renderer.texture.DynamicTexture
import net.minecraft.core.SectionPos
import net.minecraft.resources.ResourceLocation
import org.redsxi.mc.ctplus.ctPlus
import org.redsxi.mc.ctplus.mapping.Text
import org.redsxi.mc.ctplus.modId
import org.redsxi.transitplus.client.render.RenderContext
import org.redsxi.transitplus.client.render.Temporary
import org.redsxi.transitplus.client.render.rail.RailRenderContext
import org.redsxi.transitplus.common.data.ChunkPos
import org.redsxi.transitplus.common.getOrCreate
import java.awt.Color
import java.awt.Font
import java.awt.Font.PLAIN
import java.awt.image.BufferedImage
import java.awt.image.BufferedImage.TYPE_INT_ARGB
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO
import kotlin.math.pow

// Fuck
class RcpScreen: IScreen(Text.translatable("ui", "rcp")) {

    val cachedChunkRailRenderer = HashMap<ChunkPos, RailRenderContext>()

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

        val id = ResourceLocation("ctplus", "dynamic_rcp_${System.currentTimeMillis()}")

        val nImg = NativeImage.read(ByteArrayInputStream(Temporary.image))
        val texture = DynamicTexture(nImg)
        texture.setFilter(false, false)
        client.textureManager.register(id, texture)

        // Actual map render
        context.pushPose()

        context.scale(sReal, sReal)
        context.translate(translateX, translateY)

        /*
        [s^-1,   0, -wX]
        [   0,s^-1, -wY]
        [   0,   0,   1]
         */

        context.drawRect(0f, 0f, 16f, 16f, white)

        val startX = SectionPos.blockToSectionCoord(-translateX)
        val startY = SectionPos.blockToSectionCoord(-translateY)

        val endX = SectionPos.blockToSectionCoord(( windowW / sReal ) - translateX)
        val endY = SectionPos.blockToSectionCoord(( windowH / sReal ) - translateY)

        for(x in startX..endX) {
            for(y in startY..endY) {
                val pos = ChunkPos(x, y)
                val chunkRender = cachedChunkRailRenderer.getOrCreate(
                    pos,
                    RailRenderContext(
                        pos,
                        Minecraft.getInstance().level ?: throw InternalError()
                    )
                )
                //chunkRender.draw(context)
            }
        }

        context.drawLine(-10f, 100f, 10f, 100f, 0xFFFFFFFF.toInt())

        context.setShader(GameRenderer.getPositionTexShader() ?: throw RuntimeException())
        context.setShaderTexture(id)
        context.setShaderColor(1.0F, 1.0F, 1.0F, 1f)
        context.enableBlend()
        context.defaultBlendFunc()
        context.enableDepthTest()
        context.blit(0f, 0f, 16f, 16f)

        context.popPose()

        if(0 in startX..endX) {
            if(0 in startY..endY) {
                context.drawString("Chunk 00 in the viewport", 10f, 10f, white)
            } else {
                context.drawString("Chunk 00 not in the viewport", 10f, 10f, yellow)
            }
        } else {
            context.drawString("Chunk 00 not in the viewport", 10f, 10f, yellow)
        }

        client.textureManager.release(id)
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

    val bufferedImage = HashMap<ChunkPos, BufferedImage>()
}