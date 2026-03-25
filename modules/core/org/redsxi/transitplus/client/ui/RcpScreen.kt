package org.redsxi.transitplus.client.ui

import org.redsxi.mc.ctplus.mapping.Text
import org.redsxi.transitplus.client.render.RenderContext
import org.redsxi.transitplus.common.logger.logger
import kotlin.math.pow

class RcpScreen: IScreen(Text.translatable("ui", "rcp")) {

    val windowWHalf: Double get() = window.guiScaledWidth.toDouble() / 2
    val windowHHalf: Double get() = window.guiScaledHeight.toDouble() / 2

    var translateX = windowWHalf
    var translateY = windowHHalf

    var scale: Int = 0 // 1.1 ^ scale
    val sReal: Double get() = 1.1.pow(scale)

    override fun render(context: RenderContext, mouseX: Int, mouseY: Int) {

        context.drawRect(0f, 0f, window.guiScaledWidth.toFloat(), window.guiScaledHeight.toFloat(), bg)
        context.pushPose()
        context.drawString("Hello World", 10f, 10f, -1)
        context.drawString("Viewport:", 10f, 18f, debug)
        context.drawString("tX: $translateX, tY: $translateY, scale: $sReal", 10f, 26f, debug)

        context.translate(translateX, translateY)
        context.scale(sReal, sReal)

        context.drawCircle(0f, 0f, 24f, cyan)

        context.popPose()


        //context.translate(0.0, 0.0)

    }

    override fun mouseScrolled(x: Double, y: Double, sV: Double): Boolean {
        logger.info("Scroll: \nWindowW: ${window.guiScaledWidth}, WindowH: ${window.height}\nWindowSW: ${window.guiScaledWidth}, WindowSH: ${window.guiScaledHeight}\nGuiScale: ${window.guiScale}\nMouseX: $x, MouseY: $y\nScrollValue: $sV")


        val oldS = 1.1.pow(scale)
        scale += sV.toInt()
        val newS = 1.1.pow(scale)

        val dS = newS - oldS

        translateX += dS * x
        translateY += dS * y

        return super.mouseScrolled(x, y, sV)
    }

    override fun mouseDragged(d: Double, e: Double, i: Int, f: Double, g: Double): Boolean {
        if (i == 0) {
            translateX += f
            translateY += g
        }
        return super.mouseDragged(d, e, i, f, g)
    }
}