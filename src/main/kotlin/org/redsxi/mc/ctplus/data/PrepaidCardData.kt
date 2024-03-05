package org.redsxi.mc.ctplus.data

import net.minecraft.nbt.CompoundTag
import net.minecraft.world.item.ItemStack
import org.redsxi.mc.ctplus.util.Time

class PrepaidCardData(stack: ItemStack) : CardData(stack) {

    var balance: Int = 50
    var lastRechargeTime: Long = Time.millis

    override fun loadData(tag: CompoundTag) {
        super.loadData(tag)
        lastRechargeTime = tag.getLong("LastChargeTime")
        balance = tag.getInt("Balance")
    }

    override fun putDefaultData(tag: CompoundTag) {
        super.putDefaultData(tag)
        tag.putLong("LastChargeTime", Time.millis)
        tag.putInt("Balance", 50)
    }

    override fun update0(tag: CompoundTag) {
        super.update0(tag)
        tag.putLong("LastChargeTime", lastRechargeTime)
        tag.putInt("Balance", balance)
    }
}