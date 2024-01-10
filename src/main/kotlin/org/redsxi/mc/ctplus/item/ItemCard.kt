package org.redsxi.mc.ctplus.item

import net.minecraft.world.item.Item
import org.redsxi.mc.ctplus.card.Card

class ItemCard(val card: Card) : Item(Properties().stacksTo(1)) {

}