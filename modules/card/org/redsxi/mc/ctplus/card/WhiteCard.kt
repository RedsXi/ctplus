package org.redsxi.mc.ctplus.card

import org.redsxi.mc.ctplus.data.CardData

class WhiteCard : Card<CardData, WhiteCard>() {
    override fun balance(data: CardData): Int = 0

    override fun payImpl(data: CardData, price: Int): Boolean = false

    override fun rechargeImpl(data: CardData, amount: Int): Boolean = false

    override fun canOverdraft(data: CardData): Boolean = false

    override fun canRecharge(data: CardData): Boolean = false

    override fun isValid(data: CardData): Boolean = false
}