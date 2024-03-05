package org.redsxi.mc.ctplus.data

import net.minecraft.world.item.ItemStack

abstract class CardDataType<T : CardData> {
    abstract fun create(stack: ItemStack): T

    class CardDataTypeSimple<TT : CardData> internal constructor(private val deserializer: (ItemStack) -> TT) : CardDataType<TT>() {
        override fun create(stack: ItemStack): TT = deserializer(stack)
    }

    companion object {
        @JvmStatic
        fun <TTT : CardData> createSimple(deserializer: (ItemStack) -> TTT)
            = CardDataTypeSimple(deserializer)
    }
}