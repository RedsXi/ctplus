package org.redsxi.mc.ctplus.card

import net.minecraft.resources.ResourceLocation
import org.redsxi.mc.ctplus.Registries
import org.redsxi.mc.ctplus.idOf

abstract class Card {
    fun getID(): ResourceLocation = Registries.CARD.getItemID(this)

    fun getCardItemTextureLocation(): ResourceLocation = idOf("item/card/white")
    fun getCardFrontTextureLocation(): ResourceLocation = idOf("item/card/white_f")
    fun getCardBackTextureLocation(): ResourceLocation = idOf("item/card/white_b")


    abstract fun balance(): Int

    abstract fun payImpl(price: Int): Boolean

    abstract fun rechargeImpl(amount: Int): Boolean

    /**
     * Tips 已确认是否有足够的余额
     */
    fun pay(price: Int): Boolean {
        if(price < balance() || (canOverdraft())) {
            return payImpl(price)
        }
        return false
    }

    fun recharge(amount: Int): Boolean {
        if(canRecharge()) {
            return rechargeImpl(amount)
        }
        return false
    }

    abstract fun canOverdraft(): Boolean
    abstract fun canRecharge(): Boolean


    abstract fun isValid(): Boolean
}