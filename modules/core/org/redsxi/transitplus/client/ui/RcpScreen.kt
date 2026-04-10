package org.redsxi.transitplus.client.ui

import org.redsxi.mc.ctplus.mapping.Text
import org.redsxi.transitplus.client.render.RenderContext
import kotlin.math.pow

// Fuck
class RcpScreen: IScreen(Text.translatable("ui", "rcp")) {

    val windowWHalf: Double get() = window.guiScaledWidth.toDouble() / 2
    val windowHHalf: Double get() = window.guiScaledHeight.toDouble() / 2

    var translateX = windowWHalf
    var translateY = windowHHalf

    var scale: Int = 0 // 1.1 ^ scale
    val sReal: Double get() = 1.1.pow(scale)

    override fun render(context: RenderContext, mouseX: Int, mouseY: Int) {
        renderBackground(context.stack)

        // Actual map render
        context.pushPose()

        context.scale(sReal, sReal)
        context.translate(translateX, translateY)

        context.drawCircle(0f, 0f, 24f, cyan)

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
}