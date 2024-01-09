package org.redsxi.mc.crabgc.util

import net.minecraft.core.Direction
import net.minecraft.world.level.block.Block
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape

object FacingUtil {
    fun getVoxelShapeByDirection(
        x1: Double,
        y1: Double,
        z1: Double,
        x2: Double,
        y2: Double,
        z2: Double,
        facing: Direction
    ): VoxelShape {
        return when (facing) {
            Direction.NORTH -> Block.box(x1, y1, z1, x2, y2, z2)
            Direction.EAST -> Block.box(16.0 - z2, y1, x1, 16.0 - z1, y2, x2)
            Direction.SOUTH -> Block.box(16.0 - x2, y1, 16.0 - z2, 16.0 - x1, y2, 16.0 - z1)
            Direction.WEST -> Block.box(z1, y1, 16.0 - x2, z2, y2, 16.0 - x1)
            else -> Shapes.block()
        }
    }
}