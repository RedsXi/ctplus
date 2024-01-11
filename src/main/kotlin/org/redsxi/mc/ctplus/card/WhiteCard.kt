package org.redsxi.mc.ctplus.card

class WhiteCard : Card() {
    override fun balance(): Int = 0

    override fun payImpl(price: Int): Boolean = false

    override fun rechargeImpl(amount: Int): Boolean = false

    override fun canOverdraft(): Boolean = false

    override fun canRecharge(): Boolean = false

    override fun isValid(): Boolean = false
}