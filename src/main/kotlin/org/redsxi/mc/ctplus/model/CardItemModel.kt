package org.redsxi.mc.ctplus.model

import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.client.resources.model.*
import net.minecraft.resources.ResourceLocation
import org.redsxi.mc.ctplus.card.Card
import org.redsxi.mc.ctplus.modelItemGenerated
import org.redsxi.mc.ctplus.util.ArrayUtil.asMutableCollection
import java.util.function.Function

class UnbakedCardItemModel(private val card: Card) : UnbakedModel {
    override fun getDependencies(): MutableCollection<ResourceLocation> = modelItemGenerated.asMutableCollection()

    override fun resolveParents(function: Function<ResourceLocation, UnbakedModel>) {
        TODO("Not yet implemented")
    }

    override fun bake(
        modelBaker: ModelBaker,
        function: Function<Material, TextureAtlasSprite>,
        modelState: ModelState,
        resourceLocation: ResourceLocation
    ): BakedModel {

        TODO("Not yet implemented")
    }

}