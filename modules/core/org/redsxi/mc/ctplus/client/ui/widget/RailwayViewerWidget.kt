package org.redsxi.mc.ctplus.client.ui.widget

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
import net.minecraft.client.gui.components.Widget
import net.minecraft.client.renderer.GameRenderer
import net.minecraft.client.renderer.ShaderInstance
import net.minecraft.network.chat.Component

@Environment(EnvType.CLIENT)
class RailwayViewerWidget(
    x: Int,
    y: Int,
    w: Int,
    h: Int
): Widget {
    val client = Minecraft.getInstance()
    val enter = EditBox(client.font, 5, 5, 160, 20, Component.empty())

    init {
        enter.value = "30,30,100,100"
    }

    // 关于自带函数:
    // fill(PoseStack, startX, startY, endX, endY, color):
    // hLine(startX, endX, y, color)
    // endX, endY 均不包含


    override fun render(stack: PoseStack, mouseX: Int, mouseY: Int, f: Float) {
        stack.pushPose()
        // enter.render(stack, mouseX, mouseY, f)

        try {
            val n = enter.value.split(",")
            fillRect(
                stack,
                n[0].toInt(),
                n[1].toInt(),
                n[2].toInt(),
                n[3].toInt(),
                -1,
            )
        } catch(e: Exception) {
            // ignore
        }
        // 绘制状态文字


        stack.popPose()
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
        BufferUploader.drawWithShader(
            bufferBuilder.render(stack.last().pose())
        )
        RenderSystem.enableTexture()
        RenderSystem.disableBlend()
    }

    fun fillRect(
        stack: PoseStack,
        startX: Int,
        startY: Int,
        endX: Int,
        endY: Int,
        color: Int
    ) {
        doRender(stack, GameRenderer.getPositionColorShader()) { matrix ->
            // 以四边形模式开始渲染（貌似
            begin(VertexFormat.Mode.QUADS, POSITION_COLOR)
            // 标记四个顶点（左下右下右上左上
            vertex(
                matrix,
                startX.toFloat(),
                endY.toFloat(),
                1.0f
            ).color(color).endVertex()
            vertex(
                matrix,
                endX.toFloat(),
                endY.toFloat(),
                1.0f
            ).color(color).endVertex()
            vertex(
                matrix,
                endX.toFloat(),
                startY.toFloat(),
                1.0f
            ).color(color).endVertex()
            vertex(
                matrix,
                startX.toFloat(),
                startY.toFloat(),
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

    /**
     * 检测某些东西（比如鼠标）是否在控件内
     */
}