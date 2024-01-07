package org.redsxi.mc.crabgc.block

import net.minecraft.block.*
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.state.property.BooleanProperty
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import org.redsxi.mc.crabgc.blockentity.BlockEntityTicketBarrierPayDirect

class BlockTicketBarrierPayDirect : BlockEntityProvider, HorizontalFacingBlock (
    Settings.of(Material.METAL, MapColor.GRAY)
        .requiresTool()
        .strength(2F)
        .luminance {5}
        .nonOpaque()
) {
    private val open: BooleanProperty = BooleanProperty.of("open")

    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity {
        return BlockEntityTicketBarrierPayDirect(pos, state)
    }

    @Deprecated("", level = DeprecationLevel.WARNING)
    override fun onEntityCollision(state: BlockState, world: World, pos: BlockPos, entity: Entity) {
        world.updateListeners(pos, state, state, Block.NOTIFY_LISTENERS)
        if(!world.isClient && entity is PlayerEntity) {
            val facing = state.get(FACING)
            val playerPosRotated = entity
                .pos
                .subtract(pos.x+0.5,0.0,pos.z+0.5)
                .rotateY(Math.toRadians(facing.asRotation().toDouble()).toFloat())
            val open = state.get(open)
            if(open && playerPosRotated.z > 0) {
                world.setBlockState(pos, state.with(this.open, false))
            } else if (!open && playerPosRotated.z < 0) {

            }
        }
    }
}