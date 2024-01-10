package org.redsxi.mc.ctplus.model

import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel
import net.minecraft.client.renderer.block.model.BakedQuad
import net.minecraft.client.renderer.block.model.ItemOverrides
import net.minecraft.client.renderer.block.model.ItemTransforms
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.client.resources.model.*
import net.minecraft.core.Direction
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.RandomSource
import net.minecraft.world.level.block.state.BlockState
import java.util.function.Function

class CardItemModel : UnbakedModel, BakedModel, FabricBakedModel {
    override fun getDependencies(): MutableCollection<ResourceLocation> {
        TODO("Not yet implemented")
    }

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

    override fun getQuads(
        blockState: BlockState?,
        direction: Direction?,
        randomSource: RandomSource
    ): MutableList<BakedQuad> {
        TODO("Not yet implemented")
    }

    override fun useAmbientOcclusion(): Boolean {
        TODO("Not yet implemented")
    }

    override fun isGui3d(): Boolean = false

    override fun usesBlockLight(): Boolean = false

    override fun isCustomRenderer(): Boolean {
        TODO("Not yet implemented")
    }

    override fun getParticleIcon(): TextureAtlasSprite {
        TODO("Not yet implemented")
    }

    override fun getTransforms(): ItemTransforms {
        TODO("Not yet implemented")
    }

    override fun getOverrides(): ItemOverrides {
        TODO("Not yet implemented")
    }
}