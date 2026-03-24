package org.redsxi.transitplus.client.ui

import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.screens.Screen
import org.redsxi.mc.ctplus.mapping.Text
import org.redsxi.transitplus.common.logger.logger

@Deprecated("")
class RailwayControllerPanelUI: Screen(Text.translatable("ui", "rcp")) {
    val client: Minecraft = Minecraft.getInstance()

    override fun init() {
        super.init()
    }

    override fun render(poseStack: PoseStack, mouseX: Int, mouseY: Int, delta: Float) {
        poseStack.pushPose()
        poseStack.popPose()
    }

    override fun mouseDragged(mouseX: Double, mouseY: Double, isRightClick: Int, dragX: Double, dragY: Double): Boolean {
        logger.info("MouseDrag($mouseX,$mouseY,$isRightClick,$dragX,$dragY)")
        return super.mouseDragged(mouseX, mouseY, isRightClick, dragX, dragY)
    }

    override fun mouseScrolled(mouseX: Double, mouseY: Double, scrollAmount: Double): Boolean {
        logger.info("MouseScroll($mouseX,$mouseY,$scrollAmount)")
        return super.mouseScrolled(mouseX, mouseY, scrollAmount)
    }
}