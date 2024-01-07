package org.redsxi.mc.crabgc.block

import mtr.mappings.BlockDirectionalMapper
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.client.item.TooltipContext
import net.minecraft.item.ItemPlacementContext
import net.minecraft.item.ItemStack
import net.minecraft.server.world.ServerWorld
import net.minecraft.state.StateManager
import net.minecraft.state.property.BooleanProperty
import net.minecraft.text.Text
import net.minecraft.util.math.BlockPos
import net.minecraft.world.BlockView

abstract class IBlockTicketBarrier(s: Settings, private val needRedstone: Boolean) : BlockDirectionalMapper(s) {

    private val ENABLED: BooleanProperty = BooleanProperty.of("enabled")

    init {
        if(needRedstone) defaultState = defaultState.with(ENABLED, false)
    }

    fun isEnabledByRedstone(state: BlockState): Boolean {
        return if (needRedstone) state.get(ENABLED)
            else true
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        builder.add(FACING)
        if(needRedstone) builder.add(ENABLED)
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState? {
        return defaultState
    }

    override fun appendTooltip(
        stack: ItemStack?,
        world: BlockView?,
        tooltip: MutableList<Text>?,
        options: TooltipContext?
    ) {

    }

    override fun tick(state: BlockState, world: ServerWorld, pos: BlockPos) {
        if(needRedstone) {
            if (state.get(ENABLED) != world.isReceivingRedstonePower(pos)) world.setBlockState(pos, state.cycle(ENABLED))
        }
    }
}