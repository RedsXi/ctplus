package org.redsxi.mc.ctplus

import net.fabricmc.fabric.api.`object`.builder.v1.block.entity.FabricBlockEntityTypeBuilder
import net.minecraft.core.BlockPos
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import org.redsxi.mc.ctplus.block.BlockTicketBarrierPayDirect
import org.redsxi.mc.ctplus.blockentity.BlockEntityTicketBarrierPayDirect
import org.redsxi.mc.ctplus.card.WhiteCard
import org.redsxi.mc.ctplus.item.ItemCard
import org.redsxi.mc.ctplus.util.BlockEntityTypeUtil
import org.redsxi.mc.ctplus.util.ItemGroupUtil

class BETFactory<T : BlockEntity>(private val beClass: Class<T>) : BlockEntityType.BlockEntitySupplier<T>,
    FabricBlockEntityTypeBuilder.Factory<T> {
    override fun create(blockPos: BlockPos, blockState: BlockState): T {
        val constructor = beClass.getConstructor(BlockPos::class.java, BlockState::class.java)
        return constructor.newInstance(blockPos, blockState)
    }
}

// Blocks
val blockTicketBarrierPayDirect = BlockTicketBarrierPayDirect()

// Block Entities
val blockEntityTicketBarrierPayDirect: BlockEntityType<BlockEntityTicketBarrierPayDirect> = BlockEntityTypeUtil.create(blockTicketBarrierPayDirect, BlockEntityTicketBarrierPayDirect::class)

// Items
val itemCard = ItemCard()


// Item Groups
val itemGroupMain = ItemGroupUtil.create("main", ItemStack(itemCard))

// Cards
val cardWhite = WhiteCard()