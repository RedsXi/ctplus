package org.redsxi.mc.ctplus

import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.BlockEntityType
import org.redsxi.mc.ctplus.block.BlockTicketBarrierPayDirect
import org.redsxi.mc.ctplus.blockentity.BlockEntityTicketBarrierPayDirect
import org.redsxi.mc.ctplus.card.WhiteCard
import org.redsxi.mc.ctplus.item.ItemCard
import org.redsxi.mc.ctplus.util.BlockEntityTypeUtil
import org.redsxi.mc.ctplus.util.ItemGroupUtil

// Blocks
val blockTicketBarrierPayDirect = BlockTicketBarrierPayDirect()

// Block Entities
val blockEntityTicketBarrierPayDirect: BlockEntityType<BlockEntityTicketBarrierPayDirect> = BlockEntityTypeUtil.create(blockTicketBarrierPayDirect, BlockEntityTicketBarrierPayDirect::class)

// Cards
val cardWhite = WhiteCard()

// Items
val itemCard = ItemCard(cardWhite)



// Item Groups
val itemGroupMain = ItemGroupUtil.create("main", ItemStack(itemCard))