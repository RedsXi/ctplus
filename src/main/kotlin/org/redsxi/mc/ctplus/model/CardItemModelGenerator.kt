package org.redsxi.mc.ctplus.model

import net.minecraft.resources.ResourceLocation

object CardItemModelGenerator {
    fun generate(icon: ResourceLocation): String {
        return """
            {
                "parent": "item/generated",
                "textures": {
                    "layer0": "${icon.namespace}:${icon.path}"
                }
            }
        """.trimIndent()
    }
}