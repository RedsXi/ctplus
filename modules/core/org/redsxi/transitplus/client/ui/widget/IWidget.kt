package org.redsxi.transitplus.client.ui.widget

import com.mojang.blaze3d.vertex.PoseStack
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.gui.components.Widget
import net.minecraft.client.gui.components.events.GuiEventListener
import net.minecraft.client.gui.narration.NarratableEntry
import org.redsxi.transitplus.client.render.RenderContext

@Environment(EnvType.CLIENT)
abstract class IWidget: Widget, GuiEventListener, NarratableEntry {
    abstract val x: Int
    abstract val y: Int
    abstract val width: Int
    abstract val height: Int

    abstract fun render(
        context: RenderContext,
        mouseX: Int,
        mouseY: Int
    )

    override fun render(poseStack: PoseStack, i: Int, j: Int, f: Float)
        = render(RenderContext(poseStack), i, j)
}