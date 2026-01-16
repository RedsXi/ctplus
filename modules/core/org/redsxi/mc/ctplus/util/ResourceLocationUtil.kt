package org.redsxi.mc.ctplus.util

import net.minecraft.resources.ResourceLocation

object ResourceLocationUtil {
    @JvmStatic
    fun addPrefix(id: ResourceLocation, prefix: String) = ResourceLocation(id.namespace, "$prefix${id.path}")
}