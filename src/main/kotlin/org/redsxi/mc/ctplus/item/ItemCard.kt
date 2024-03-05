package org.redsxi.mc.ctplus.item

import net.minecraft.network.chat.Component
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level
import org.redsxi.mc.ctplus.CardRegistries
import org.redsxi.mc.ctplus.card.Card
import org.redsxi.mc.ctplus.data.CardData
import org.redsxi.mc.ctplus.mapping.Text
import org.redsxi.mc.ctplus.mapping.Text.CARD
import org.redsxi.mc.ctplus.mapping.Text.TOOLTIP

class ItemCard<CardT : Card<CardDataT, CardT>, CardDataT : CardData>(val card: CardT) : Item(Properties().stacksTo(1)) {
    override fun appendHoverText(
        itemStack: ItemStack,
        level: Level?,
        list: MutableList<Component>,
        tooltipFlag: TooltipFlag
    ) {
        list.add(Text.translatable(TOOLTIP, "transit_plus_part"))

        val item = itemStack.item
        if(item is ItemCard<*, *>) {
            val card = item.card
            val context = card.context(itemStack)
            list.add(Text.empty())
            list.add(Text.translatable(TOOLTIP, "card_information"))
            context.appendCardInformation(list)
        }
    }

    override fun getName(itemStack: ItemStack): Component {
        val id = CardRegistries.CARD.getItemID(card)
        return Text.translatable(CARD, id.namespace, id.path)
    }
}