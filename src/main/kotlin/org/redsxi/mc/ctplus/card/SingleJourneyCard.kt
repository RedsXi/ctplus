package org.redsxi.mc.ctplus.card

class SingleJourneyCard(private val price: Int) : Card() {

    private var isUsed = false

    override fun balance(): Int = price

    override fun payImpl(price: Int): Boolean {
        if(isUsed) return false
        isUsed = true
        return true
    }

    override fun rechargeImpl(amount: Int) = false

    override fun canOverdraft() = false

    override fun canRecharge() = false

    override fun isValid() = !isUsed
}