package org.redsxi.mc.ctplus.mapping

import net.fabricmc.fabric.impl.itemgroup.ItemGroupHelper
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntityType

object RegistryMapper {
    private fun getBlockRegistry(): Registry<Block> = BuiltInRegistries.BLOCK
    private fun getBlockEntityTypeRegistry(): Registry<BlockEntityType<*>> = BuiltInRegistries.BLOCK_ENTITY_TYPE
    private fun getItemRegistry(): Registry<Item> = BuiltInRegistries.ITEM

    private fun <T> register(registry: Registry<T>, location: ResourceLocation, item: T & Any): T = Registry.register(registry, location, item)
    fun registerBlock(location: ResourceLocation, item: Block): Block = register(getBlockRegistry(), location, item)
    fun registerBlockEntityType(location: ResourceLocation, item: BlockEntityType<*>): BlockEntityType<*> = register(
        getBlockEntityTypeRegistry(), location, item)
    fun registerItem(location: ResourceLocation, item: Item): Item = register(getItemRegistry(), location, item)

    fun registerItemGroup(location: ResourceLocation, item: CreativeModeTab) =
        ItemGroupHelper.appendItemGroup(item)
}