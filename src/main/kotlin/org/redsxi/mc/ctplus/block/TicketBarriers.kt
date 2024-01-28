package org.redsxi.mc.ctplus.block

import mtr.SoundEvents
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.EntityBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import org.redsxi.bool.False
import org.redsxi.mc.ctplus.blockentity.BlockEntityTicketBarrierPayDirect
import org.redsxi.mc.ctplus.core.PassManager
import org.redsxi.mc.ctplus.core.TransitPlus
import org.redsxi.mc.ctplus.mapping.Text
import org.redsxi.mc.ctplus.mapping.Text.BLOCK

open class BlockTicketBarrierPayDirect : EntityBlock, BlockTicketBarrier() {
    override fun newBlockEntity(blockPos: BlockPos, blockState: BlockState): BlockEntity =
        BlockEntityTicketBarrierPayDirect(blockPos, blockState, False)

    override fun appendHoverText(
        itemStack: ItemStack,
        blockGetter: BlockGetter?,
        tooltip: MutableList<Component>,
        tooltipFlag: TooltipFlag
    ) {
        tooltip.add(Text.translatable(Text.TOOLTIP, "ticket_barrier_pay_direct"))
    }

    override fun process(pos: BlockPos, level: Level, player: Player): Boolean =
        PassManager.onEntityPass(pos, level, player, SoundEvents.TICKET_BARRIER)
}

class BlockTicketBarrierPayDirectTP : BlockTicketBarrierPayDirect() {
    override fun appendHoverText(
        itemStack: ItemStack,
        blockGetter: BlockGetter?,
        tooltip: MutableList<Component>,
        tooltipFlag: TooltipFlag
    ) {
        super.appendHoverText(itemStack, blockGetter, tooltip, tooltipFlag)
        tooltip.add(Text.translatable(Text.TOOLTIP, "within_transit_plus"))
    }

    override fun process(pos: BlockPos, level: Level, player: Player): Boolean =
        pass(player, pos, level, TransitPlus.PassType.PAY_DIRECT)


}

class BlockTicketBarrierEntranceTP : BlockTicketBarrier() {
    override fun appendHoverText(
        itemStack: ItemStack,
        blockGetter: BlockGetter?,
        list: MutableList<Component>,
        tooltipFlag: TooltipFlag
    ) {
        list.add(Text.translatable(Text.TOOLTIP, "within_transit_plus"))
    }

    override fun process(pos: BlockPos, level: Level, player: Player): Boolean =
        pass(player, pos, level, TransitPlus.PassType.ENTRY)

    override fun getName(): MutableComponent = Text.translatable(BLOCK, "mtr", "ticket_barrier_entrance_1") as MutableComponent
}

class BlockTicketBarrierExitTP : BlockTicketBarrier() {
    override fun appendHoverText(
        itemStack: ItemStack,
        blockGetter: BlockGetter?,
        list: MutableList<Component>,
        tooltipFlag: TooltipFlag
    ) {
        list.add(Text.translatable(Text.TOOLTIP, "within_transit_plus"))
    }

    override fun process(pos: BlockPos, level: Level, player: Player): Boolean =
        pass(player, pos, level, TransitPlus.PassType.EXIT)

    override fun getName(): MutableComponent = Text.translatable(BLOCK, "mtr", "ticket_barrier_exit_1") as MutableComponent
}