package org.redsxi.mc.ctplus.item

import net.minecraft.network.chat.Component
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level
import org.redsxi.mc.ctplus.Registries
import org.redsxi.mc.ctplus.card.Card
import org.redsxi.mc.ctplus.mapping.Text

class ItemCard(val card: Card) : Item(Properties().stacksTo(1)) {
    override fun appendHoverText(
        itemStack: ItemStack,
        level: Level?,
        list: MutableList<Component>,
        tooltipFlag: TooltipFlag
    ) {
        list.add(Text.toolTip("transit_plus_part"))
        val compound = itemStack.tag
        if(compound != null) {
            list.add(Text.empty())
            list.add(Text.toolTip("card_information"))
            card.loadData(compound)
            card.appendCardInformation(list)
        }

    }

    override fun getName(itemStack: ItemStack): Component {
        val id = Registries.CARD.getItemID(card)
        return Text.card(id.namespace, id.path)
    }
}