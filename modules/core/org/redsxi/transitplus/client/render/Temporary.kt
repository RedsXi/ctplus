package org.redsxi.transitplus.client.render

import java.awt.BasicStroke
import java.awt.Color
import java.awt.Font
import java.awt.GradientPaint
import java.awt.Stroke
import java.awt.geom.Arc2D
import java.awt.geom.Arc2D.OPEN
import java.awt.geom.Line2D
import java.awt.image.BufferedImage
import java.awt.image.BufferedImage.TYPE_INT_ARGB
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO
import kotlin.math.PI

object Temporary {

    val image: ByteArray

    val R=15.0
    val Start=23.561944901923447
    val H=14.999999999999996
    val End=-47.12388980384689
    val K=15.0

    const val PI2 = PI * 2
    const val R2D = 180.0 / PI



    fun radLimit(rad: Double): Double {
        var result = rad
        while(result < 0.0) {
            result += PI2
        }
        while(result >= PI2) {
            result -= PI2
        }
        return result
    }

    fun rad2deg(r: Double, negLimit: Double = 0.0): Double {
        var result = r * R2D
        while(result < negLimit) {
            result += 360.0
        }
        while(result >= 360.0) {
            result -= 360.0
        }
        return result
    }



    init {

        /*
        {Start: {





        {Rails: {000003C000000FC4000000000000FFC4: , 0000000000000FC4000003C00000FFC4: {Start: {kO: 0.0d, Start: 0.0d, kX: 0.7071067811865476d, End: 21.213203435596423d, kY: 0.7071067811865475d, Straight: 1b}, End: {R: 0.0d, Start: 0.0d, H: 0.0d, End: 0.0d, K: 0.0d, Straight: 1b}, Direction: 3}}, ChunkPos: {ChunkY: 0, ChunkX: 0}}

         */
        val img = BufferedImage(256, 256, TYPE_INT_ARGB)
        val renderer = img.createGraphics()

        renderer.color = Color.BLACK
        renderer.fillRect(0, 0, 256, 256)

        renderer.color = Color.YELLOW
        renderer.stroke = BasicStroke(3f * 16)

        val k = 16.0

        val dA = radLimit(End) - radLimit(Start)



        val s = Line2D.Double(
            0.0,
            0.0,
            (0.7071067811865476 * 21.213203435596423) * k,
            (0.7071067811865475 * 21.213203435596423) * k
        )
//
        val e = Arc2D.Double(
            (H - R) * k,
            (K - R) * k,
            R * k * 2,
            R * k * 2,
            -rad2deg(Start),
            rad2deg(End - Start, -360.0),
            OPEN
        )

        renderer.draw(s)
        renderer.color = Color.RED
        renderer.draw(e)

        val output = ByteArrayOutputStream()
        ImageIO.write(img, "png", output)
        image = output.toByteArray()
    }
}
