package org.redsxi.mc.ctplus.util

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import org.redsxi.mc.ctplus.Variables

@Environment(EnvType.CLIENT)
object MTRTranslation {
    @JvmStatic
    fun getTranslation(name: String): String = name.replace("|", "")
}