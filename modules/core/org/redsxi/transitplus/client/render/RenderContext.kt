package org.redsxi.transitplus.client.render

import com.mojang.blaze3d.platform.Window
import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.BufferBuilder
import com.mojang.blaze3d.vertex.BufferUploader
import com.mojang.blaze3d.vertex.DefaultVertexFormat.POSITION_COLOR
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.Tesselator
import com.mojang.blaze3d.vertex.VertexFormat
import com.mojang.logging.LogUtils
import com.mojang.math.Matrix4f
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GameRenderer
import net.minecraft.client.renderer.ShaderInstance
import org.redsxi.mc.ctplus.generated.RuntimeVariables
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin


/**
 * 专门用于渲染操作的类，类似于新版 Minecraft 的 GuiGraphics
 * @see PoseStack
 * @see RenderSystem
 */
// TODO 将RenderSystem启用禁用的东西全部移动到实际绘制函数外
class RenderContext(val stack: PoseStack) {

    private val logger = LogUtils.getLogger()

    fun pushPose() = stack.pushPose()
    fun popPose() = stack.popPose()

    private val debug = RuntimeVariables.DEBUG
    private val displayAsFrameworks = false

    val window: Window get() = Minecraft.getInstance().window
    val guiScale: Int get() = window.guiScale.toInt()

    /**
     * 渲染区域裁剪：用于部分区域渲染不干扰区外内容
     *
     * 取消裁剪使用 [cancelScissor]
     */
    fun scissorRenderArea(
        x: Int,
        y: Int,
        w: Int,
        h: Int
    ) = scissorRenderArea0(
        x * guiScale,
        y * guiScale,
        w  * guiScale,
        h * guiScale
    )

    fun scissorRenderArea0(
        x: Int,
        y: Int,
        w: Int,
        h: Int
    ) {
        val targetY = window.height - y - h
        RenderSystem.enableScissor(
            x,
            targetY,
            w,
            h
        )
    }

    /**
     * @see scissorRenderArea
     */
    fun cancelScissor() {
        RenderSystem.disableScissor()
    }

    private fun render(
        render: BufferBuilder.(Matrix4f) -> BufferBuilder.RenderedBuffer
    ) {
        val builder = Tesselator.getInstance().builder
        BufferUploader.drawWithShader(
            builder.render(stack.last().pose())
        )
    }

    /**
     * 画三角形的
     */
    fun renderTriangle(
        aX: Float,
        aY: Float,
        aColor: Int,
        bX: Float,
        bY: Float,
        bColor: Int,
        cX: Float,
        cY: Float,
        cColor: Int
    ){
        RenderSystem.enableBlend()
        RenderSystem.disableTexture()
        RenderSystem.defaultBlendFunc()
        RenderSystem.setShader{ GameRenderer.getPositionColorShader() }
        render {
            begin(VertexFormat.Mode.TRIANGLES, POSITION_COLOR)
            vertex(it, aX, aY, 1f).color(aColor).endVertex()
            vertex(it, bX, bY, 1f).color(bColor).endVertex()
            vertex(it, cX, cY, 1f).color(cColor).endVertex()
            end()
        }
        RenderSystem.enableTexture()
        RenderSystem.disableBlend()
    }

    /**
     * 画圆的
     */
    fun drawCircle(
        centerX: Int,
        centerY: Int,
        radius: Int,
        color: Int
    ) {
        RenderSystem.enableBlend()
        RenderSystem.disableTexture()
        RenderSystem.defaultBlendFunc()
        RenderSystem.setShader{ GameRenderer.getPositionColorShader() }
        render {
            begin(VertexFormat.Mode.TRIANGLE_FAN, POSITION_COLOR)
            vertex(it, centerX.toFloat(), centerY.toFloat(), 1f).color(color).endVertex()
            val accuracy = radius * 4
            for (k in accuracy * 2 downTo 0) {
                val vertexX = radius * sin(PI * ( k / accuracy.toDouble() )).toFloat()
                val vertexY = radius * cos(PI * ( k / accuracy.toDouble() )).toFloat()
                vertex(
                    it,
                    centerX + vertexX,
                    centerY - vertexY,
                    1f
                ).color(color).endVertex()
            }
            end()
        }
        RenderSystem.enableTexture()
        RenderSystem.disableBlend()
    }

    /**
     * 此物不工作，勿用
     *
     * 画圆环的
     */
    // TODO 修复不显示 Tip: 顶点必须逆时针
    fun drawRing(
        centerX: Int,
        centerY: Int,
        radius: Int,
        strokeWidth: Int,
        color: Int
    ) {
        RenderSystem.enableBlend()
        RenderSystem.disableTexture()
        RenderSystem.defaultBlendFunc()
        RenderSystem.setShader{ GameRenderer.getPositionColorShader() }
        render {
            begin(VertexFormat.Mode.TRIANGLE_STRIP, POSITION_COLOR)
            vertex(it, centerX.toFloat(), centerY.toFloat(), 1f).color(color).endVertex()
            val accuracy = radius * 4
            for (k in accuracy * 2 downTo 0) {
                val kX = sin(PI * ( k / accuracy.toDouble() )).toFloat()
                val kY = cos(PI * ( k / accuracy.toDouble() )).toFloat()
                val innerX = kX * (radius - ( strokeWidth / 2f ) )
                val innerY = kY * (radius - ( strokeWidth / 2f ) )
                val outerX = kX * (radius + ( strokeWidth / 2f ) )
                val outerY = kY * (radius + ( strokeWidth / 2f ) )

                vertex(
                    it,
                    centerX + innerX,
                    centerY - innerY,
                    1f
                ).color(color).endVertex()
                vertex(
                    it,
                    centerX + outerX,
                    centerY - outerY,
                    1f
                ).color(color).endVertex()
            }
            end()
        }
        RenderSystem.enableTexture()
        RenderSystem.disableBlend()
    }
}