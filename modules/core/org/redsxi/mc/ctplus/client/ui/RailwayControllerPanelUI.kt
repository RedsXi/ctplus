package org.redsxi.mc.ctplus.client.ui

import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.screens.Screen
import org.redsxi.mc.ctplus.mapping.Text


class RailwayControllerPanelUI: Screen(Text.translatable("ui", "rcp")) {
    val button = Button(0, 0, 0, 20, Text.translatable("ui", "button")) {

    }

    override fun init() {
        super.init()
        button.width = 150
        button.x = 10
        button.y = 10
        addRenderableWidget(button)
    }

    override fun render(poseStack: PoseStack, mouseX: Int, mouseY: Int, delta: Float) {
        renderBackground(poseStack)
        poseStack.pushPose()
        super.render(poseStack, mouseX, mouseY, delta)
        poseStack.popPose()
    }
}