package org.redsxi.mc.ctplus.block

import mtr.SoundEvents
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape
import org.redsxi.mc.ctplus.Properties.OPEN
import org.redsxi.mc.ctplus.core.TransitPlus
import org.redsxi.mc.ctplus.mapping.BarrierBlockMapper
import org.redsxi.mc.ctplus.util.FacingUtil

open class BlockTicketBarrier : BarrierBlockMapper() {
    open fun needRedstone() = true

    open fun process(pos: BlockPos, level: Level, player: Player): Boolean = false

    override fun entityInside(state: BlockState, world: Level, pos: BlockPos, entity: Entity) {
        world.sendBlockUpdated(pos, state, state, Block.UPDATE_CLIENTS)
        if(!world.isClientSide && entity is Player) {
            val facing = state.getValue(FACING)
            val playerPosRotated = entity
                .position()
                .subtract(pos.x+0.5,0.0,pos.z+0.5)
                .yRot(Math.toRadians(facing.toYRot().toDouble()).toFloat())
            val open = state.getValue(OPEN)
            if(open && playerPosRotated.z > 0) {
                world.setBlockAndUpdate(pos, state.setValue(OPEN, false))
            } else if (!open && playerPosRotated.z < 0) {
                val newOpen = process(pos, world, entity)
                world.setBlockAndUpdate(pos, state.setValue(OPEN, newOpen))
                if(!newOpen && !world.blockTicks.hasScheduledTick(pos, this)) {
                    world.scheduleTick(pos, this, 40)
                }
            }
        }
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        builder.add(org.redsxi.mc.ctplus.Properties.HORIZONTAL_FACING, OPEN)
    }

    override fun tick(
        state: BlockState,
        level: ServerLevel,
        pos: BlockPos
    ) {
        level.setBlockAndUpdate(pos, state.setValue(OPEN, false))
    }

    override fun getStateForPlacement(blockPlaceContext: BlockPlaceContext): BlockState? {
        return defaultBlockState()
            .setValue(FACING, blockPlaceContext.horizontalDirection)
            .setValue(OPEN, false)
    }

    override fun getShape(
        blockState: BlockState,
        blockGetter: BlockGetter,
        blockPos: BlockPos,
        collisionContext: CollisionContext
    ): VoxelShape {
        val facing = blockState.getValue(FACING)
        return FacingUtil.getVoxelShapeByDirection(
            12.0,
            0.0,
            0.0,
            16.0,
            15.0,
            16.0,
            facing
        )
    }

    override fun getCollisionShape(
        blockState: BlockState,
        blockGetter: BlockGetter,
        blockPos: BlockPos,
        collisionContext: CollisionContext
    ): VoxelShape {
        val facing = blockState.getValue(FACING)
        val open = blockState.getValue(OPEN)
        val base = FacingUtil.getVoxelShapeByDirection(
            15.0,
            0.0,
            0.0,
            16.0,
            24.0,
            16.0,
            facing
        )
        val append = FacingUtil.getVoxelShapeByDirection(
            0.0,
            0.0,
            7.0,
            16.0,
            24.0,
            9.0,
            facing
        )
        return if(open) {
            base
        } else {
            Shapes.or(append, base)
        }
    }

    fun pass(player: Player, position: BlockPos, world: Level, passType: TransitPlus.PassType): Boolean {
        return TransitPlus.pass(player, position, world, SoundEvents.TICKET_BARRIER, passType).get()
    }
}