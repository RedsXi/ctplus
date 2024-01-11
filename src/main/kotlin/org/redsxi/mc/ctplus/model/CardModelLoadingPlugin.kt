package org.redsxi.mc.ctplus.model

import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin.Context
import org.redsxi.mc.ctplus.cardWhite

class CardModelLoadingPlugin : ModelLoadingPlugin {
    override fun onInitializeModelLoader(pluginContext: Context) {
        pluginContext.modifyModelOnLoad().register { ori, id ->
            val modelId = id.id()
            CardItemModelFactory.getModel(cardWhite)
        }
    }
}