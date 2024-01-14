package org.redsxi.mc.ctplus.mapping

import net.minecraft.network.chat.Component
import org.redsxi.mc.ctplus.modId

object Text {
    private fun translatable(type: String, name: String): Component {
        return Component.translatable("${type}.${modId}.${name}")
    }

    private fun translatable(type: String, name: String, vararg objects: Any): Component {
        return Component.translatable("${type}.${modId}.${name}", *objects)
    }

    fun itemGroup(id: String): Component {
        return translatable("itemGroup", id)
    }

    fun gui(id: String): Component {
        return translatable("gui", id)
    }

    fun gui(id: String, vararg objects: Any): Component {
        return translatable("gui", id, *objects)
    }

    fun toolTip(id: String, vararg objects: Any): Component {
        return translatable("tooltip", id, *objects)
    }

    fun card(ns: String, id: String): Component = Component.translatable("card.$ns.$id")
}