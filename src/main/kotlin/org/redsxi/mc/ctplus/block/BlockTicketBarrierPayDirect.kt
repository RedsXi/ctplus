package org.redsxi.mc.ctplus.block

import mtr.SoundEvents
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.EntityBlock
import net.minecraft.world.level.block.HorizontalDirectionalBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.material.Material
import net.minecraft.world.level.material.MaterialColor
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape
import org.redsxi.mc.ctplus.Properties.HORIZONTAL_FACING
import org.redsxi.mc.ctplus.Properties.OPEN
import org.redsxi.mc.ctplus.blockentity.BlockEntityTicketBarrierPayDirect
import org.redsxi.mc.ctplus.util.FacingUtil
import org.redsxi.mc.ctplus.util.PassManager

class BlockTicketBarrierPayDirect : EntityBlock, HorizontalDirectionalBlock (
    Properties.of(Material.METAL, MaterialColor.COLOR_GRAY).requiresCorrectToolForDrops().strength(2.0F)
) {
    override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity {
        return BlockEntityTicketBarrierPayDirect(pos, state)
    }

    @Deprecated("", level = DeprecationLevel.WARNING)
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
                val newOpen = PassManager.onEntityPass(pos, world, entity, SoundEvents.TICKET_PROCESSOR_ENTRY)
                world.setBlockAndUpdate(pos, state.setValue(OPEN, newOpen))
                if(!newOpen && !world.blockTicks.hasScheduledTick(pos, this)) {
                    world.scheduleTick(pos, this, 40)
                }
            }
        }
    }

    override fun appendHoverText(
        stack: ItemStack,
        blockGetter: BlockGetter?,
        tooltip: MutableList<Component>,
        options: TooltipFlag
    ) {
        super.appendHoverText(stack, blockGetter, tooltip, options)
        tooltip.add(Component.translatable("tooltip.cgcem.ticket_barrier_pay_direct"))
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        builder.add(HORIZONTAL_FACING, OPEN)
    }

    override fun tick(
        blockState: BlockState,
        serverLevel: ServerLevel,
        blockPos: BlockPos,
        randomSource: RandomSource
    ) {
        super.tick(blockState, serverLevel, blockPos, randomSource)
        serverLevel.setBlockAndUpdate(blockPos, blockState.setValue(OPEN, false))
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
}