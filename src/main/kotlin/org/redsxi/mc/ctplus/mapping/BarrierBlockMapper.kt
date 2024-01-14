package org.redsxi.mc.ctplus.mapping

import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.level.block.EntityBlock
import net.minecraft.world.level.block.HorizontalDirectionalBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.Material
import net.minecraft.world.level.material.MaterialColor

abstract class BarrierBlockMapper : EntityBlock, HorizontalDirectionalBlock(Properties.of(Material.METAL, MaterialColor.COLOR_GRAY).requiresCorrectToolForDrops().strength(2.0F)) {
    override fun tick(
        blockState: BlockState,
        serverLevel: ServerLevel,
        blockPos: BlockPos,
        randomSource: RandomSource
    ) {
        tick(blockState, serverLevel, blockPos)
    }

    open fun tick(state: BlockState, level: ServerLevel, pos: BlockPos) = Unit
}