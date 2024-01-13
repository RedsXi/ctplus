package org.redsxi.mc.ctplus

import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.item.Item
import org.redsxi.mc.ctplus.card.Card
import java.util.UUID

object Variables {
    var playerList: Map<UUID,ServerPlayer> = HashMap()
    var cardItemList: Map<Card, Item> = HashMap()
}