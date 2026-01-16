package org.redsxi.mc.ctplus.data

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import net.minecraft.network.chat.Component
import org.redsxi.mc.ctplus.CTPlusRegistries
import org.redsxi.mc.ctplus.card.Card
import org.redsxi.mc.ctplus.util.value

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

    fun toWeb() = Web(this)

    class Web(context: CardContext<*, *>) {
        val type: String
        val data: CardData

        init {
            type = CTPlusRegistries.CARD.getItemID(context.card).toString()
            data = context.data
        }

        class JsonAdapter: TypeAdapter<Web>() {
            override fun write(out: JsonWriter, value: Web) {
                out.beginObject()
                out.name("type").value(value.type)
                out.name("data").value(value.data.json())
                out.endObject()
            }

            override fun read(input: JsonReader): Web? {
                return null
            }
        }
    }
}