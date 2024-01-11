package org.redsxi.mc.ctplus

import net.minecraft.world.item.Item
import org.redsxi.mc.ctplus.card.Card
import java.util.UUID

object Variables {
    var playerList: Map<UUID,String> = HashMap()
    var cardItemList: Map<Card, Item> = HashMap()
}