package org.redsxi.mc.ctplus.util

import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState

@SuppressWarnings("any")
object BlockEntityTypeUtil {
    private fun <T : BlockEntity> create(block: Block, blockEntitySupplier: (BlockPos, BlockState) -> T): BlockEntityType<T> {
        val builder = BlockEntityType.Builder.of(blockEntitySupplier, block)
        return builder.build(null)
    }

    fun <T : BlockEntity> create(block: Block, blockEntityClass: Class<T>, vararg arguments: Any): BlockEntityType<T> {

        val classArray = ArrayList<Class<out Any>>()
        classArray.add(BlockPos::class.java)
        classArray.add(BlockEntity::class.java)
        arguments.forEach {
            classArray.add(it::class.java)
        }

        val array = Array<Class<out Any>>(0) {BlockEntityTypeUtil::class.java}

        val constructor = blockEntityClass.getConstructor(*classArray.toArray(array))
        return create(
            block
        ) {
            pos, state -> constructor.newInstance(pos, state, *arguments)
        }
    }
}