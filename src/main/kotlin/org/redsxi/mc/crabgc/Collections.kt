package org.redsxi.mc.crabgc

import net.fabricmc.fabric.api.`object`.builder.v1.block.entity.FabricBlockEntityTypeBuilder
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.text.Text
import net.minecraft.util.math.BlockPos
import org.redsxi.mc.crabgc.blockentity.BlockEntityTicketBarrierPayDirect
import org.redsxi.mc.crabgc.block.BlockTicketBarrierPayDirect

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
        Registries.BLOCK_ENTITY_TYPE,
        ticketBarrierPayDirect,
        FabricBlockEntityTypeBuilder.create(
            BETFactory(BlockEntityTicketBarrierPayDirect::class.java)
        ).build()
    )

// Items
val itemBlockTicketBarrier = BlockItem(blockTicketBarrierPayDirect, Item.Settings())


// Item Groups
val itemGroupMain = ItemGroup.create(ItemGroup.Row.TOP,0)
    .displayName(Text.translatable("itemGroup.cgcem.main"))
    .entries { _,entries ->
        entries.add(itemBlockTicketBarrier)
    }

