package org.redsxi.mc.ctplus.data

import net.minecraft.network.chat.Component
import org.redsxi.mc.ctplus.card.Card

@Suppress("UNUSED", "UNCHECKED_CAST")
class CardContext<CardDataT : CardData, CardT : Card<CardDataT, CardT>>(cardO: Card<*, *>, dataO: CardData) {
    val card: CardT
    val data: CardDataT

    init {
        card = cardO as CardT
        data = dataO as CardDataT
    }


    fun balance() = card.balance(data)

    fun discountFactor() = card.discountFactor()

    fun pay(price: Int) = card.pay(data, price)
    fun recharge(amount: Int) = card.recharge(data, amount)

    fun canOverdraft() = card.canOverdraft(data)
    fun canRecharge() = card.canRecharge(data)
    fun isValid() = card.isValid(data)


    fun update() = data.update()

    fun appendCardInformation(list: MutableList<Component>) = card.appendCardInformation(data, list)
}