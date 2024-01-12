package org.redsxi.mc.ctplus.model

import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin.Context
import net.minecraft.resources.ResourceLocation
import org.redsxi.mc.ctplus.Registries
import org.slf4j.LoggerFactory

@Deprecated("Bad")
class CardModelLoadingPlugin : ModelLoadingPlugin {

    private val logger = LoggerFactory.getLogger("CardModelLoadingPlugin")

    override fun onInitializeModelLoader(pluginContext: Context) {
        pluginContext.modifyModelOnLoad().register { ori, id ->
            logger.info(id.id().toString())
            if(id.id().path.length <= 10) return@register ori
            if(!id.id().path.startsWith("item/card_")) return@register ori

            val cardId = ResourceLocation(id.id().namespace, id.id().path.substring(10, id.id().path.length - 10))
            logger.info(cardId.toString())
            if(Registries.CARD.contains(cardId)) {
                return@register CardItemModelFactory.getModel(Registries.CARD[cardId])
            } else {
                return@register ori
            }
        }
    }
}