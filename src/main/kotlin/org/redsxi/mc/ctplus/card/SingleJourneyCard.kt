package org.redsxi.mc.ctplus.card

import net.minecraft.nbt.CompoundTag
import net.minecraft.resources.ResourceLocation
import org.redsxi.mc.ctplus.idOf

class SingleJourneyCard : Card() {

    private var price: Int = 2

    private var isUsed = true

    override fun balance(): Int = if(isUsed) {
        0
    } else {
        price
    }

    override fun payImpl(price: Int): Boolean {
        if(isUsed) return false
        isUsed = true
        return true
    }

    override fun rechargeImpl(amount: Int) = false

    override fun canOverdraft() = false

    override fun canRecharge() = false

    override fun isValid() = !isUsed

    override fun getCardItemTextureLocation(): ResourceLocation {
        return idOf("item/card/single_journey")
    }

    override fun loadData(nbt: CompoundTag) {
        price = nbt.getInt("Price")
        isUsed = nbt.getBoolean("IsUsed")
    }

    override fun saveData(nbt: CompoundTag): CompoundTag {
        nbt.putInt("Price", price)
        nbt.putBoolean("IsUsed", isUsed)
        return nbt
    }
}