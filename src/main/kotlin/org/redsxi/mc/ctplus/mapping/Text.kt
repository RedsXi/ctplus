package org.redsxi.mc.ctplus.mapping

import net.minecraft.network.chat.Component
import org.redsxi.mc.ctplus.modId

object Text {

    const val BLOCK = "block"
    const val CARD = "card"
    const val GUI = "gui"
    const val TOOLTIP = "tooltip"
    const val ITEM_GROUP = "itemGroup"

    @JvmStatic
    fun translatable(type: String, modId: String, name: String, vararg objects: Any): Component
        = Component.translatable("$type.$modId.$name", *objects)
    @JvmStatic
    fun literal(str: String): Component
        = Component.literal(str)

    @JvmStatic
    fun translatable(type: String, name: String, vararg objects: Any): Component
        = translatable(type, modId, name, *objects)



    @JvmStatic
    fun empty(): Component = literal("")
}