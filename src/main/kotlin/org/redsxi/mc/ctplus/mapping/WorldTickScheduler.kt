package org.redsxi.mc.ctplus.mapping

import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block

object WorldTickScheduler {
    fun scheduleBlockTick(level: Level, pos: BlockPos, block: Block, delay: Int) {
        level.scheduleTick(pos, block, delay)
    }
}