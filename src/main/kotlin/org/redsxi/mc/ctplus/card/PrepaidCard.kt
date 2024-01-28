package org.redsxi.mc.ctplus.card

import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import org.redsxi.mc.ctplus.idOf
import org.redsxi.mc.ctplus.mapping.Text
import org.redsxi.mc.ctplus.mapping.Text.TOOLTIP
import org.redsxi.mc.ctplus.util.Date
import org.redsxi.mc.ctplus.util.Time

class PrepaidCard : Card() {
    override fun balance(): Int = balance

    override fun payImpl(price: Int): Boolean { balance -= price; return true }
    override fun rechargeImpl(amount: Int): Boolean { balance += amount; return true }

    override fun canOverdraft(): Boolean = true
    override fun canRecharge(): Boolean = true

    override fun isValid(): Boolean = valid

    private var balance: Int = 5
    private var lastRechargeTime: Long = Time.millis
    private val valid: Boolean
        get() {
            return Time.millis < lastRechargeTime + EXPIRE_TIME
        }

    override fun loadData(nbt: CompoundTag) {
        lastRechargeTime = nbt.getLong("LastChargeTime")
        balance = nbt.getInt("Balance")
    }
    override fun saveData(nbt: CompoundTag): CompoundTag {
        super.saveData(nbt)
        nbt.putLong("LastChargeTime", lastRechargeTime)
        nbt.putInt("Balance", balance)
        return nbt
    }

    override fun appendCardInformation(list: MutableList<Component>) {
        super.appendCardInformation(list)
        list.add(Text.translatable(TOOLTIP, "card_balance", balance))
        list.add(Text.translatable(TOOLTIP, "card_last_recharge_time", Date[lastRechargeTime] as Any))
        if(!valid) {
            list.add(Text.translatable(TOOLTIP, "card_invalid"))
        }
    }

    override fun getCardItemTextureLocation(): ResourceLocation {
        return idOf("item/card/prepaid")
    }

    companion object {
        const val EXPIRE_TIME: Long = 0x134FD9000
    }
}