package org.redsxi.transitplus.client.ui

import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.screens.Screen
import org.redsxi.transitplus.client.ui.widget.RailwayViewerWidget
import org.redsxi.mc.ctplus.mapping.Text


class RailwayControllerPanelUI: Screen(Text.translatable("ui", "rcp")) {
    val client: Minecraft = Minecraft.getInstance()
    val railwayViewerWidget = RailwayViewerWidget(4, 12, client.window.guiScaledWidth - 8, client.window.guiScaledHeight - 16)
    override fun init() {
        super.init()
        addRenderableWidget(railwayViewerWidget.enter)
    }

    override fun render(poseStack: PoseStack, mouseX: Int, mouseY: Int, delta: Float) {
        renderBackground(poseStack)
        poseStack.pushPose()
        val screen = client.window
        drawString(poseStack, client.font, Text.literal("Screen:\nwidth=${screen.width}, height=${screen.height}\nguiScale=${screen.guiScale}\nguiScaledWidth=${screen.guiScaledWidth}, guiScaledHeight=${screen.guiScaledHeight}"), 4, 4, -1)

        railwayViewerWidget.render(poseStack, mouseX, mouseY, delta)
        super.render(poseStack, mouseX, mouseY, delta)
        poseStack.popPose()
    }
}