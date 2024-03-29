package org.redsxi.mc.ctplus.card

import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import org.redsxi.mc.ctplus.data.SingleJourneyCardData
import org.redsxi.mc.ctplus.idOf
import org.redsxi.mc.ctplus.mapping.Text

class SingleJourneyCard : Card<SingleJourneyCardData, SingleJourneyCard>() {

    /*
    DEPRECATED CODE AT 2024-3-3 -CTPLUS

    var price: Int = 2

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

    override fun appendCardInformation(list: MutableList<Component>) {
        super.appendCardInformation(list)
        list.add(Text.translatable(Text.TOOLTIP, "price", price))
        if(isUsed) {
            list.add(Text.translatable(Text.TOOLTIP, "card_is_used"))
        }
    }
    */

    override fun balance(data: SingleJourneyCardData): Int = if(data.isUsed) 0 else data.price

    override fun payImpl(data: SingleJourneyCardData, price: Int): Boolean {
        data.isUsed = true
        return true
    }

    override fun rechargeImpl(data: SingleJourneyCardData, amount: Int): Boolean = false

    override fun canOverdraft(data: SingleJourneyCardData): Boolean = false

    override fun canRecharge(data: SingleJourneyCardData): Boolean = false

    override fun isValid(data: SingleJourneyCardData): Boolean = !data.isUsed

    override fun appendCardInformation(data: SingleJourneyCardData, list: MutableList<Component>) {
        super.appendCardInformation(data, list)
        list.add(Text.translatable(Text.TOOLTIP, "price", data.price))
        if(data.isUsed) {
            list.add(Text.translatable(Text.TOOLTIP, "card_is_used"))
        }
    }

    override fun getCardItemTextureLocation(): ResourceLocation {
        return idOf("item/card/single_journey")
    }
}