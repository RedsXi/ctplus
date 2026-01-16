package org.redsxi.mc.ctplus.card

import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import org.redsxi.mc.ctplus.data.PrepaidCardData
import org.redsxi.mc.ctplus.idOf
import org.redsxi.mc.ctplus.mapping.Text
import org.redsxi.mc.ctplus.mapping.Text.TOOLTIP
import org.redsxi.mc.ctplus.util.Date
import org.redsxi.mc.ctplus.util.Time

class PrepaidCard : Card<PrepaidCardData, PrepaidCard>() {
    /* DEPRECATED CODE 2024-3-4
    private val valid: Boolean
        get() {
            return Time.millis < lastRechargeTime + EXPIRE_TIME
        }

    override fun appendCardInformation(list: MutableList<Component>) {
        super.appendCardInformation(list)
        list.add(Text.translatable(TOOLTIP, "card_balance", balance))
        list.add(Text.translatable(TOOLTIP, "card_last_recharge_time", Date[lastRechargeTime] as Any))
        if(!valid) {
            list.add(Text.translatable(TOOLTIP, "card_invalid"))
        }
    }
    */

    override fun getCardItemTextureLocation(): ResourceLocation {
        return idOf("item/card/prepaid")
    }

    override fun isValid(data: PrepaidCardData) = Time.millis < data.lastRechargeTime + EXPIRE_TIME
    override fun canRecharge(data: PrepaidCardData) = isValid(data)
    override fun canOverdraft(data: PrepaidCardData) = isValid(data)

    override fun rechargeImpl(data: PrepaidCardData, amount: Int): Boolean {
        val b = data.balance
        data.balance += amount
        return b < data.balance
    }

    override fun payImpl(data: PrepaidCardData, price: Int): Boolean {
        val b = data.balance
        data.balance -= price
        return b >= data.balance
    }

    override fun balance(data: PrepaidCardData) = data.balance

    override fun appendCardInformation(data: PrepaidCardData, list: MutableList<Component>) {
        super.appendCardInformation(data, list)
        list.add(Text.translatable(TOOLTIP, "card_balance", data.balance))
        list.add(Text.translatable(TOOLTIP, "card_last_recharge_time", Date[data.lastRechargeTime] as Any))
        if(!isValid(data)) list.add(Text.translatable(TOOLTIP, "card_invalid"))
    }

    companion object {
        const val EXPIRE_TIME: Long = 0x134FD9000
    }
}