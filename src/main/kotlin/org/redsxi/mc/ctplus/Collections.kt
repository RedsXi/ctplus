package org.redsxi.mc.crabgc

import net.fabricmc.fabric.api.`object`.builder.v1.block.entity.FabricBlockEntityTypeBuilder
import net.minecraft.core.BlockPos
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.chat.Component
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import org.redsxi.mc.crabgc.blockentity.BlockEntityTicketBarrierPayDirect
import org.redsxi.mc.crabgc.block.BlockTicketBarrierPayDirect
import org.redsxi.mc.crabgc.item.ItemCard

class BETFactory<T : BlockEntity>(private val beClass: Class<T>) : FabricBlockEntityTypeBuilder.Factory<T> {
    override fun create(blockPos: BlockPos, blockState: BlockState): T {
        val constructor = beClass.getConstructor(BlockPos::class.java, BlockState::class.java)
        return constructor.newInstance(blockPos, blockState)
    }
}

// Blocks
val blockTicketBarrierPayDirect = BlockTicketBarrierPayDirect()

// Block Entities
val blockEntityTicketBarrierPayDirect: BlockEntityType<BlockEntityTicketBarrierPayDirect> =
    Registry.register(
        BuiltInRegistries.BLOCK_ENTITY_TYPE,
        ticketBarrierPayDirect,
        FabricBlockEntityTypeBuilder.create(
            BETFactory(BlockEntityTicketBarrierPayDirect::class.java)
        ).build()
    )

// Items
val itemCard = ItemCard()


// Item Groups
val itemGroupMain: CreativeModeTab = CreativeModeTab.builder(CreativeModeTab.Row.TOP,0)
    .title(Component.translatable("itemGroup.cgcem.main"))
    .icon { ItemStack(itemCard) }
    .build()

