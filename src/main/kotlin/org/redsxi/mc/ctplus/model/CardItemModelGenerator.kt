package org.redsxi.mc.ctplus.model

import net.minecraft.resources.ResourceLocation
import org.slf4j.LoggerFactory

object CardItemModelGenerator {
    private val LOGGER = LoggerFactory.getLogger("CardItemModelGen")
    fun generate(icon: ResourceLocation): String {
        val str = """
            {
                "parent": "item/generated",
                "textures": {
                    "layer0": "${icon.namespace}:${icon.path}"
                }
            }
        """.trimIndent()
        LOGGER.debug("Model: $str")
        return str
    }
}