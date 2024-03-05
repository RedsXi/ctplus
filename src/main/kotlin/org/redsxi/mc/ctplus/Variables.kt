package org.redsxi.mc.ctplus

import net.minecraft.server.level.ServerPlayer
import java.util.UUID

object Variables {
    var playerList: MutableMap<UUID, ServerPlayer> = HashMap()

    var translationIndex: Int = 0
}