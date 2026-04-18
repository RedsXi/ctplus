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
import com.mojang.math.Vector3f
import kotlinx.serialization.EncodeDefault
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiComponent
import net.minecraft.client.renderer.GameRenderer
import net.minecraft.client.renderer.GameRenderer.getPositionColorShader
import net.minecraft.client.renderer.ShaderInstance
import net.minecraft.network.chat.Component
import org.redsxi.mc.ctplus.generated.RuntimeVariables
import org.redsxi.transitplus.common.annotation.InnerApi
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

    val client: Minecraft get() = Minecraft.getInstance()
    val window: Window get() = client.window
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
    ) = scissorRenderArea0( // H 30 L 90 CHICK 15 RAB 15
        x * guiScale,
        y * guiScale,
        w  * guiScale,
        h * guiScale
    )

    @InnerApi
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

    @InnerApi
    fun render(
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
    fun drawTriangle(
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
        RenderSystem.setShader{ getPositionColorShader() }
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
        centerX: Float,
        centerY: Float,
        radius: Float,
        color: Int
    ) {
        RenderSystem.enableBlend()
        RenderSystem.disableTexture()
        RenderSystem.defaultBlendFunc()
        RenderSystem.setShader{ getPositionColorShader() }
        render {
            begin(VertexFormat.Mode.TRIANGLE_FAN, POSITION_COLOR)
            vertex(it, centerX, centerY, 1f).color(color).endVertex()
            val accuracy = radius.toInt() * 4
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
     * 画圆环的
     */
    fun drawRing(
        centerX: Float,
        centerY: Float,
        radius: Float,
        strokeWidth: Float,
        color: Int
    ) {
        RenderSystem.enableBlend()
        RenderSystem.disableTexture()
        RenderSystem.defaultBlendFunc()
        RenderSystem.setShader{ getPositionColorShader() }
        render {
            begin(VertexFormat.Mode.TRIANGLE_STRIP, POSITION_COLOR)
            //vertex(it, centerX.toFloat(), centerY.toFloat(), 1f).color(color).endVertex()
            val accuracy = radius.toInt() * 4
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

    /**
     * 启用材质
     */
    fun enableTexture() = RenderSystem.enableTexture()

    /**
     * 禁用材质
     */
    fun disableTexture() = RenderSystem.disableTexture()

    /**
     * 启用混合
     */
    fun enableBlend() = RenderSystem.enableBlend()

    /**
     * 禁用混合
     */
    fun disableBlend() = RenderSystem.disableBlend()

    /**
     * 使用默认混合方式
     */
    fun defaultBlendFunc() = RenderSystem.defaultBlendFunc()

    /**
     * 修改着色器
     *
     * 各种着色器参见[GameRenderer]
     *
     * @see GameRenderer.getPositionColorShader
     * @see GameRenderer.getPositionColorTexShader
     * @see GameRenderer.getPositionTexShader
     */
    fun setShader(shader: ShaderInstance) = RenderSystem.setShader { shader }

    /**
     * 画矩形
     */
    fun drawRect(
        x: Float,
        y: Float,
        w: Float,
        h: Float,
        color: Int
    ) {
        defaultBlendFunc()
        disableTexture()
        enableBlend()

        setShader(getPositionColorShader() ?: throw RuntimeException())

        render {
            begin(
                VertexFormat.Mode.QUADS,
                POSITION_COLOR
            )
            vertex(
                it,
                x,
                y + h,
                1.0f
            ).color(color).endVertex()
            vertex(
                it,
                x + w,
                y + h,
                1.0f
            ).color(color).endVertex()
            vertex(
                it,
                x + w,
                y,
                1.0f
            ).color(color).endVertex()
            vertex(
                it,
                x,
                y,
                1.0f
            ).color(color).endVertex()

            end()
        }

        disableBlend()
        enableTexture()
    }

    /**
     * 矩形描边
     */
    fun drawRectBorder(
        x: Float,
        y: Float,
        w: Float,
        h: Float,
        strokeWidth: Float,
        strokeColor: Int
    ) {
        val sHalf = strokeWidth / 2

        // 左边界
        drawRect(
            x - sHalf,
            y - sHalf,
            strokeWidth,
            h + strokeWidth,
            strokeColor
        )

        // 上边界
        drawRect(
            x - sHalf,
            y - sHalf,
            w + strokeWidth,
            strokeWidth,
            strokeColor
        )
    }

    /**
     * 绘制文字
     */

    fun drawString(
        text: String,
        x: Float,
        y: Float,
        color: Int
    )
        = client.font.draw(stack, text, x, y, color)

    /**
     * 坐标变换（平移）
     */
    fun translate(tX: Double, tY: Double, tZ: Double = 0.0)
        = stack.translate(tX, tY, 0.0)


    /**
     * 缩放
     */
    fun scale(sX: Double, sY: Double, sZ: Double = 1.0)
        = stack.scale(sX.toFloat(), sY.toFloat(), sZ.toFloat())

    fun drawLine(sX: Float, sY: Float, eX: Float, eY: Float, color: Int) {
        render {
            begin(VertexFormat.Mode.DEBUG_LINES, POSITION_COLOR)
            vertex(it, sX, sY, 0f).color(color).endVertex()
            vertex(it, eX, eY, 0f).color(color).endVertex()
            end()
        }
    }
}