package org.redsxi.mc.ctplus

import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.item.Item
import org.redsxi.mc.ctplus.card.Card
import org.redsxi.mc.ctplus.event.PlayerEvent
import java.util.UUID

object Variables {
    var playerList: MutableMap<UUID,ServerPlayer> = HashMap()
    init {
        PlayerEvent.JOIN.register {
            playerList[it.uuid] = it
        }
        PlayerEvent.LEFT.register {
            playerList.remove(it.uuid)
        }
    }
}