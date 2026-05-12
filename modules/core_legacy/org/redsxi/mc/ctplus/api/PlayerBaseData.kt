package org.redsxi.mc.ctplus.api

import net.minecraft.world.entity.player.Player

class PlayerBaseData(player: Player) {
    val name: String
    val uuid: String

    init {
        name = player.gameProfile.name
        uuid = player.stringUUID
    }
}