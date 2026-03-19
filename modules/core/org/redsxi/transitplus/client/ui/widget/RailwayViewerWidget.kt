package org.redsxi.transitplus.client.ui.widget

import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.BufferBuilder
import com.mojang.blaze3d.vertex.BufferUploader
import com.mojang.blaze3d.vertex.DefaultVertexFormat.POSITION_COLOR
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.Tesselator
import com.mojang.blaze3d.vertex.VertexFormat
import com.mojang.math.Matrix4f
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.components.EditBox
import net.minecraft.client.gui.narration.NarratableEntry
import net.minecraft.client.gui.narration.NarrationElementOutput
import net.minecraft.client.renderer.GameRenderer
import net.minecraft.client.renderer.ShaderInstance
import net.minecraft.network.chat.Component
import org.redsxi.transitplus.client.render.RenderContext

@Environment(EnvType.CLIENT)
class RailwayViewerWidget(
    override val x: Int,
    override val y: Int,
    override val width: Int,
    override val height: Int
): IWidget() {
    val client = Minecraft.getInstance()
    val enter = EditBox(client.font, 5, 70, 160, 20, Component.empty())

    init {
        enter.value = "20,20,20,80,80,20"
    }

    // 关于自带函数:
    // fill(PoseStack, startX, startY, endX, endY, color):
    // hLine(startX, endX, y, color)
    // endX, endY 均不包含

    override fun render(context: RenderContext, mouseX: Int, mouseY: Int) {
        try {
            val n = enter.value.split(",")
            context.drawRect(x.toFloat(), y.toFloat(), width.toFloat(), height.toFloat(), 0xFF000000.toInt())
            context.drawRectBorder(x.toFloat(), y.toFloat(), width.toFloat(), height.toFloat(), 1f, 0xFFFFFFFF.toInt())
            context.drawCircle(160f, 100f, 30f, 0xFFC00080.toInt())
            context.drawRing(80f, 100f, 25f, 4f,0xFF00C0C0.toInt())
            context.drawTriangle(
                n[0].toFloat(),
                n[1].toFloat(),
                0xFFFF0000.toInt(),
                n[2].toFloat(),
                n[3].toFloat(),
                0xFF00FF00.toInt(),
                n[4].toFloat(),
                n[5].toFloat(),
                0xFF0000FF.toInt()
            )
            context.scissorRenderArea(20,20,30,30)
            context.drawTriangle(
                n[0].toFloat(),
                n[1].toFloat(),
                0xFFFFFFFF.toInt(),
                n[2].toFloat(),
                n[3].toFloat(),
                0xFFFFFFFF.toInt(),
                n[4].toFloat(),
                n[5].toFloat(),
                0xFFFFFFFF.toInt()
            )
            context.cancelScissor()
            //context.demo()
        } catch(e: Exception) {
            // ignore
        }

    }

    fun doRender(
        stack: PoseStack,
        shader: ShaderInstance?,
        render: BufferBuilder.(Matrix4f) -> BufferBuilder.RenderedBuffer
    ) {
        val bufferBuilder = Tesselator.getInstance().builder
        RenderSystem.enableBlend()
        RenderSystem.disableTexture()
        RenderSystem.defaultBlendFunc()
        RenderSystem.setShader{shader}

        RenderSystem.enableScissor(20,30,50,80) // x, y, w, h

        BufferUploader.drawWithShader(
            bufferBuilder.render(stack.last().pose())
        )

        RenderSystem.disableScissor()

        RenderSystem.enableTexture()
        RenderSystem.disableBlend()
    }


    fun fillRect(
        stack: PoseStack,
        startX: Float,
        startY: Float,
        endX: Float,
        endY: Float,
        color: Int
    ) {
        doRender(stack, GameRenderer.getPositionColorShader()) { matrix ->
            // 以四边形模式开始渲染（貌似
            begin(VertexFormat.Mode.QUADS, POSITION_COLOR)
            // 标记四个顶点（左下右下右上左上
            vertex(
                matrix,
                startX,
                endY,
                1.0f
            ).color(color).endVertex()
            vertex(
                matrix,
                endX,
                endY,
                1.0f
            ).color(color).endVertex()
            vertex(
                matrix,
                endX,
                startY,
                1.0f
            ).color(color).endVertex()
            vertex(
                matrix,
                startX,
                startY,
                1.0f
            ).color(color).endVertex()
            end()
        }
    }

    /**
     * 绘制竖直线路
     */
    fun drawVerticalLine(
        stack: PoseStack,
        y: Int,
        startX: Int,
        endX: Int,
        color: Int
    ) {
    }

    /**
     * 绘制水平线路
     */
    fun drawHorizontalLine(
        stack: PoseStack,
        x: Int, // startY
        startY: Int, //endY
        endY: Int, //x
        color: Int
    ) {

    }

    override fun narrationPriority(): NarratableEntry.NarrationPriority? = null

    override fun updateNarration(narrationElementOutput: NarrationElementOutput) {

    }

    /**
     * 检测某些东西（比如鼠标）是否在控件内
     */
}