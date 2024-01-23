package org.redsxi.mc.ctplus.core

import net.minecraft.resources.ResourceLocation
import org.redsxi.mc.ctplus.mapping.Text
import org.redsxi.mc.ctplus.mapping.Text.ITEM_GROUP

class ItemGroupBuilder private constructor(val id: ResourceLocation) {
    val name = Text.translatable(ITEM_GROUP, id)
}