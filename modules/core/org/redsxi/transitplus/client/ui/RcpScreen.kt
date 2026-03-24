package org.redsxi.transitplus.client.ui

import net.minecraft.client.gui.components.Button
import org.redsxi.mc.ctplus.mapping.Text
import org.redsxi.transitplus.client.render.RenderContext
import org.redsxi.transitplus.common.logger.logger
import java.lang.Math.pow
import kotlin.math.pow

class RcpScreen: IScreen(Text.translatable("ui", "rcp")) {

    var translateX = windowWHalf
    var translateY = windowHHalf
    var scale = 1.0
    val windowWHalf: Double get() = window.guiScaledWidth.toDouble() / 2
    val windowHHalf: Double get() = window.guiScaledHeight.toDouble() / 2

    override fun render(context: RenderContext, mouseX: Int, mouseY: Int) {

        context.drawRect(0f, 0f, window.guiScaledWidth.toFloat(), window.guiScaledHeight.toFloat(), bg)
        context.pushPose()
        context.drawString("", 0f, 0f, -1)


        context.translate(translateX, translateY)
        context.scale(scale, scale)

        context.drawCircle(0f, 0f, 24f, cyan)

        context.popPose()
        //context.translate(0.0, 0.0)

    }

    override fun mouseScrolled(d: Double, e: Double, f: Double): Boolean {
        val newScale = scale + f

        // TODO 缩放时围绕鼠标缩放

        scale += f
        return super.mouseScrolled(d, e, f)
    }

    override fun mouseDragged(d: Double, e: Double, i: Int, f: Double, g: Double): Boolean {
        translateX += f
        translateY += g
        return super.mouseDragged(d, e, i, f, g)
    }
}