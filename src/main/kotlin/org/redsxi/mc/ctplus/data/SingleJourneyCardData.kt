package org.redsxi.mc.ctplus.data

import net.minecraft.nbt.CompoundTag
import net.minecraft.world.item.ItemStack

class SingleJourneyCardData(stack: ItemStack) : CardData(stack) {
    var price: Int = 2
    var isUsed = true

    override fun loadData(tag: CompoundTag) {
        super.loadData(tag)
        price = tag.getInt("Price")
        isUsed = tag.getBoolean("IsUsed")
    }

    override fun putDefaultData(tag: CompoundTag) {
        super.putDefaultData(tag)
        tag.putInt("Price", 0)
        tag.putBoolean("IsUsed", true)
    }

    override fun update0(tag: CompoundTag) {
        super.update0(tag)
        tag.putInt("Price", price)
        tag.putBoolean("IsUsed", isUsed)
    }
}