package org.redsxi.mc.ctplus.data

import net.minecraft.CrashReport
import net.minecraft.ReportedException
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.item.ItemStack
import org.redsxi.mc.ctplus.CTPlusRegistries
import org.redsxi.mc.ctplus.card.Card
import org.redsxi.mc.ctplus.item.ItemCard

open class CardData(val stack: ItemStack) {

    fun load() {
        val itemTag = stack.tag ?: CompoundTag()
        val cardDataTag = itemTag.getCompound("CardData")
        if(cardDataTag.isEmpty) putDefaultData(cardDataTag)
        loadData(cardDataTag)
    }

    fun update() {
        val tag = CompoundTag()
        val cardDataTag = CompoundTag()
        update0(cardDataTag)
        tag.put("CardData", cardDataTag)
        stack.tag = tag
    }

    var entryZoneEncoded: Float = Float.MIN_VALUE
    var entryStationName: String = ""
    var isEntered = false
    protected open fun loadData(tag: CompoundTag) {
        entryZoneEncoded = tag.getFloat("EntryZone")
        entryStationName = tag.getString("EntryStation")
        isEntered = tag.getBoolean("IsEntered")
    }

    protected open fun putDefaultData(tag: CompoundTag) {
        tag.putFloat("EntryZone", Float.MIN_VALUE)
        tag.putString("EntryStation", "")
        tag.putBoolean("IsEntered", false)
    }

    protected open fun update0(tag: CompoundTag) {
        tag.putFloat("EntryZone", entryZoneEncoded)
        tag.putString("EntryStation", entryStationName)
        tag.putBoolean("IsEntered", isEntered)
    }

    companion object {
        @JvmStatic
        @Suppress("UNCHECKED_CAST")
        fun <CDT: CardData, CT: Card<CDT, CT>> deserialize(stack: ItemStack): CardContext<CDT, CT> {
            try {
                val cardItem = stack.item as ItemCard<CT, CDT>
                val card = cardItem.card
                val id = card.id
                val data = CTPlusRegistries.CARD_DATA_TYPE[id].create(stack) as CDT
                data.load()
                return CardContext(card, data)
            } catch (e : ClassCastException) {
                throw ReportedException(CrashReport.forThrowable(e, "Card type doesn't match"))
            }
        }

        @JvmStatic
        fun <CDT: CardData, CT: Card<CDT, CT>> create(card: CT): CardContext<CDT, CT>
            = deserialize(ItemStack(card.item))
    }
}