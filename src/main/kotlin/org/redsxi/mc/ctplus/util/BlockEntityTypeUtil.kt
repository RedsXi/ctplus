package org.redsxi.mc.ctplus.util

import net.minecraft.Util
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import kotlin.reflect.KClass

@SuppressWarnings("any")
object BlockEntityTypeUtil {



    fun <T : BlockEntity> create(block: Block, blockEntitySupplier: (BlockPos, BlockState) -> T): BlockEntityType<T> {
        val builder = BlockEntityType.Builder.of(blockEntitySupplier, block)
        return builder.build(null)
    }

    fun <T : BlockEntity> create(block: Block, blockEntityClass: KClass<T>): BlockEntityType<T> {
        val constructor = blockEntityClass.java.getConstructor(BlockPos::class.java, BlockState::class.java)
        return create(
            block
        ) {
            pos, state -> constructor.newInstance(pos, state)
        }
    }
}