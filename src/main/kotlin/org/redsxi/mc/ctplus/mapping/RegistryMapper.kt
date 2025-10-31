package org.redsxi.mc.ctplus.mapping

import net.minecraft.core.Registry
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntityType

object RegistryMapper {
    private fun getBlockRegistry(): Registry<Block> = Registry.BLOCK
    private fun getBlockEntityTypeRegistry(): Registry<BlockEntityType<*>> = Registry.BLOCK_ENTITY_TYPE
    private fun getItemRegistry(): Registry<Item> = Registry.ITEM

    private fun <T> register(registry: Registry<T>, location: ResourceLocation, item: T & Any): T = Registry.register(registry, location, item)
    fun registerBlock(location: ResourceLocation, item: Block): Block = register(getBlockRegistry(), location, item)
    fun registerBlockEntityType(location: ResourceLocation, item: BlockEntityType<*>): BlockEntityType<*> = register(
        getBlockEntityTypeRegistry(), location, item)
    fun registerItem(location: ResourceLocation, item: Item): Item = register(getItemRegistry(), location, item)

    fun registerItemGroup(location: ResourceLocation, item: CreativeModeTab) =
        Unit // ItemGroupHelper.appendItemGroup(item)
}