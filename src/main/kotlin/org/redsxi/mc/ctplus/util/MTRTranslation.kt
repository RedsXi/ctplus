package org.redsxi.mc.ctplus.util

import com.mojang.authlib.minecraft.client.MinecraftClient
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component
import net.minecraft.world.level.Level
import org.redsxi.mc.ctplus.Collections
import org.redsxi.mc.ctplus.mapping.Text

@Environment(EnvType.CLIENT)
object MTRTranslation {
    @JvmStatic
    fun getTranslation(level: Level, name: String): String {
        val nameArray = name.split("|")
        if(nameArray.size == 1) return name
        return nameArray[level.gameRules.getInt(Collections.Rules.TRANSLATE_INDEX), 0]
    }

    private operator fun List<String>.get(index: Int, defaultIndex: Int): String {
        if(size > index) return this[defaultIndex]
        return this[index]
    }
}