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
    fun getChineseTranslation(level: Level, name: String): Component {
        val nameArray = name.split("|")
        if(nameArray.size == 1) return Text.literal(name)
        val str = nameArray[level.gameRules.getInt(Collections.Rules.CHINESE_TRANSLATE_INDEX), 0]
        return Text.literal(str)
    }

    @JvmStatic
    fun getEnglishTranslation(level: Level, name: String): Component {
        val nameArray = name.split("|")
        if(nameArray.size == 1) return Text.literal(name)
        val str = nameArray[level.gameRules.getInt(Collections.Rules.ENGLISH_TRANSLATE_INDEX), 1]
        return Text.literal(str)
    }

    private operator fun List<String>.get(index: Int, defaultIndex: Int): String {
        if(size > index) return this[defaultIndex]
        return this[index]
    }
}