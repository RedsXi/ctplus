package org.redsxi.mc.ctplus.util

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import org.redsxi.mc.ctplus.Variables

@Environment(EnvType.CLIENT)
object MTRTranslation {
    @JvmStatic
    fun getTranslation(name: String): String {
        val nameArray = name.split("|")
        if(nameArray.size == 1) return name
        return nameArray[Variables.translationIndex, 0]
    }

    private operator fun List<String>.get(index: Int, defaultIndex: Int): String {
        if(size > index) return this[defaultIndex]
        return this[index]
    }
}