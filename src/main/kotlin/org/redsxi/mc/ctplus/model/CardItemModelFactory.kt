package org.redsxi.mc.ctplus.model

import com.mojang.datafixers.util.Either
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.renderer.block.model.BlockModel
import net.minecraft.client.renderer.block.model.ItemTransforms
import net.minecraft.client.renderer.texture.TextureAtlas
import net.minecraft.client.resources.model.Material
import net.minecraft.client.resources.model.UnbakedModel
import org.redsxi.mc.ctplus.card.Card
import org.redsxi.mc.ctplus.modelItemGenerated
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.spongepowered.include.com.google.common.collect.Lists

@Environment(EnvType.CLIENT)
interface CardItemModelFactory {

    fun getModel(card: Card): UnbakedModel

    companion object Instance : CardItemModelFactory {
        private val logger: Logger = LoggerFactory.getLogger("CardItemModelFactory")
        private var loadCount: Int = 0

        override fun getModel(card: Card): UnbakedModel {
            loadCount++
            val map = HashMap<String, Either<Material, String>>()
            map["layer0"] = Either.left(Material(TextureAtlas.LOCATION_BLOCKS, card.getCardItemTextureLocation()))
            return BlockModel(
                modelItemGenerated,
                Lists.newArrayList(),
                map,
                null, null,
                ItemTransforms.NO_TRANSFORMS,
                Lists.newArrayList()
            )
        }

        fun printModelCount() {
            logger.info("Created $loadCount card models")
        }
    }
}