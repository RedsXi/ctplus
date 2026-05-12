package org.redsxi.transitplus.client.ui

import com.mojang.blaze3d.platform.Window
import com.mojang.blaze3d.vertex.PoseStack
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component
import org.redsxi.transitplus.client.render.RenderContext

@Environment(EnvType.CLIENT)
abstract class IScreen(component: Component) : Screen(component) {
    val client: Minecraft = Minecraft.getInstance()
    val window: Window = client.window

    override fun init() {
        super.init()
    }

    override fun render(poseStack: PoseStack, i: Int, j: Int, f: Float)
        = render(RenderContext(poseStack), i, j)

    open fun render(
        context: RenderContext,
        mouseX: Int,
        mouseY: Int
    ) = Unit

    val width: Int get() = window.guiScaledWidth
}