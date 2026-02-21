package org.redsxi.mc.capi

import net.minecraft.network.chat.Component

open class Card {
    fun use() {

    }

    open fun getName(): Component = Component.literal("Card") // REPLACE LATER
}