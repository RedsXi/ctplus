@file:JvmName("MainClient")

package org.redsxi.transitplus.client

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.minecraft.nbt.NbtOps
import net.minecraft.nbt.NbtUtils
import org.redsxi.transitplus.client.network.NetworkClient
import org.redsxi.transitplus.client.web.WebServer
import org.redsxi.transitplus.common.data.ChunkPos
import org.redsxi.transitplus.common.data.rail.ChunkRail

fun entry() {
    ClientCommandRegistrationCallback.EVENT.register { dispatcher, _ ->
        dispatcher.register(ClientCommandManager.literal("rail").executes {
            CoroutineScope(Dispatchers.IO).launch {
                it.source.sendFeedback(NbtUtils.toPrettyComponent(ChunkRail.CODEC.encodeStart(NbtOps.INSTANCE, NetworkClient.getChunkRail(ChunkPos(0, 0), it.source.player.level)).result().get()))
            }
            0
        })
    }
    WebServer.start()
}