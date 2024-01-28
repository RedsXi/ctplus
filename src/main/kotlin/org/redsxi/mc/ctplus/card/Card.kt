package org.redsxi.mc.ctplus.card

import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.Level
import org.redsxi.bool.Bool
import org.redsxi.bool.False
import org.redsxi.mc.ctplus.Registries
import org.redsxi.mc.ctplus.idOf
import org.redsxi.mc.ctplus.item.ItemCard
import org.redsxi.mc.ctplus.mapping.Text
import org.redsxi.mc.ctplus.mapping.Text.GUI
import org.redsxi.mc.ctplus.util.MTROptionalData
import org.redsxi.mc.ctplus.util.MTRTranslation

abstract class Card {
    val item = ItemCard(this)

    fun getID(): ResourceLocation = Registries.CARD.getItemID(this)

    open fun getCardItemTextureLocation(): ResourceLocation = idOf("item/card/white")
    fun getCardFrontTextureLocation(): ResourceLocation = idOf("item/card/white_f")
    fun getCardBackTextureLocation(): ResourceLocation = idOf("item/card/white_b")


    abstract fun balance(): Int
    open fun discountFactor(): Float = 1F

    abstract fun payImpl(price: Int): Boolean
    abstract fun rechargeImpl(amount: Int): Boolean

    /**
     * Tips 已确认是否有足够的余额/是否可预支
     */
    fun pay(price: Int): Boolean {
        val actualPrice = (price * discountFactor()).toInt()
        if(actualPrice <= balance() || (canOverdraft() && balance() >= 0)) {
            return payImpl(actualPrice)
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

    open fun loadData(nbt: CompoundTag) {
        entryZoneEncoded = nbt.getFloat("EntryZone")
        entryStationName = nbt.getString("EntryStation")
        isEntered = Bool.fromK(nbt.getBoolean("IsEntered"))
    }
    open fun saveData(nbt: CompoundTag): CompoundTag {
        nbt.putFloat("EntryZone", entryZoneEncoded)
        nbt.putString("EntryStation", entryStationName)
        nbt.putBoolean("IsEntered", isEntered.getK())
        return nbt
    }

    fun createData(): CompoundTag {
        val compound = CompoundTag()
        saveData(compound)
        return compound
    }

    open fun getPassMessage(): Component = Text.translatable(GUI, "passed_barrier")

    open fun appendCardInformation(list: MutableList<Component>) = Unit
    open fun appendCardInformation(list: MutableList<Component>, level: Level) {
        appendCardInformation(list)
        if(!isEntered.getK()) return
        list.add(Text.translatable(GUI, "enter_at_station", MTRTranslation.getTranslation(level, entryStationName)))
    }

    // For exits and entrances

    var entryZoneEncoded: Float = Float.MIN_VALUE
    var entryStationName: String = ""
    var isEntered: Bool = False
}