package org.redsxi.mc.ctplus

import net.fabricmc.fabric.api.`object`.builder.v1.block.entity.FabricBlockEntityTypeBuilder
import net.minecraft.core.BlockPos
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.chat.Component
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import org.redsxi.mc.ctplus.blockentity.BlockEntityTicketBarrierPayDirect
import org.redsxi.mc.ctplus.block.BlockTicketBarrierPayDirect
import org.redsxi.mc.ctplus.item.ItemCard

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
    .title(Component.translatable("itemGroup.ctplus.main"))
    .icon { ItemStack(itemCard) }
    .build()

