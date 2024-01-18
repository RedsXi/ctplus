package org.redsxi.mc.ctplus.card

import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component

class PrepaidCard : Card() {
    override fun balance(): Int = TODO("Not yet implemented")

    override fun payImpl(price: Int): Boolean = TODO("Not yet implemented")
    override fun rechargeImpl(amount: Int): Boolean = TODO("Not yet implemented")

    override fun canOverdraft(): Boolean = TODO("Not yet implemented")
    override fun canRecharge(): Boolean = TODO("Not yet implemented")

    override fun isValid(): Boolean = TODO("Not yet implemented")

    override fun loadData(nbt: CompoundTag) = Unit
    override fun saveData(nbt: CompoundTag): CompoundTag = nbt

    override fun appendCardInformation(list: MutableList<Component>) = Unit
}