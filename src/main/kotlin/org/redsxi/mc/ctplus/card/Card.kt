package org.redsxi.mc.ctplus.card

import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import org.redsxi.mc.ctplus.CTPlusRegistries
import org.redsxi.mc.ctplus.data.CardContext
import org.redsxi.mc.ctplus.data.CardData
import org.redsxi.mc.ctplus.idOf
import org.redsxi.mc.ctplus.item.ItemCard
import org.redsxi.mc.ctplus.mapping.Text
import org.redsxi.mc.ctplus.mapping.Text.GUI
import org.redsxi.mc.ctplus.mapping.Text.TOOLTIP
import org.redsxi.mc.ctplus.util.MTRTranslation

@Suppress("UNCHECKED_CAST")
abstract class Card<CardDataT : CardData, CardT : Card<CardDataT, CardT>> {
    private var itemOpt: ItemCard<CardT, CardDataT>? = null

    val item: ItemCard<CardT, CardDataT>
        get() {
            return itemOpt ?: throw NullPointerException("Item of card wasn't initialized")
        }

    fun initItem() {
        if(itemOpt != null) return
        itemOpt = ItemCard(this as CardT)
    }

    val id: ResourceLocation get() = CTPlusRegistries.CARD.getItemID(this)

    open fun getCardItemTextureLocation(): ResourceLocation = idOf("item/card/white")
    fun getCardFrontTextureLocation(): ResourceLocation = idOf("item/card/white_f")
    fun getCardBackTextureLocation(): ResourceLocation = idOf("item/card/white_b")


    abstract fun balance(data: CardDataT): Int
    open fun discountFactor(): Float = 1F

    abstract fun payImpl(data: CardDataT, price: Int): Boolean
    abstract fun rechargeImpl(data: CardDataT, amount: Int): Boolean

    /**
     * Tips 已确认是否有足够的余额/是否可预支
     */
    fun pay(data: CardDataT, price: Int): Boolean {
        val actualPrice = (price * discountFactor()).toInt()
        return if(actualPrice <= balance(data) || (canOverdraft(data) && balance(data) >= 0)) {
            payImpl(data, actualPrice)
        } else false
    }

    fun recharge(data: CardDataT, amount: Int): Boolean {
        if(canRecharge(data)) {
            return rechargeImpl(data, amount)
        }
        return false
    }

    abstract fun canOverdraft(data: CardDataT): Boolean
    abstract fun canRecharge(data: CardDataT): Boolean
    abstract fun isValid(data: CardDataT): Boolean

    open fun appendCardInformation(data: CardDataT, list: MutableList<Component>) {
        if(data.isEntered) {
            list.add(
                Text.translatable(
                    TOOLTIP,
                    "enter_at_station",
                    MTRTranslation.getTranslation(data.entryStationName) as Any
                )
            )
        }
    }

    override fun toString(): String {
        return javaClass.simpleName
    }

    fun context(stack: ItemStack): CardContext<CardDataT, CardT> =
        CardData.deserialize(stack)
}