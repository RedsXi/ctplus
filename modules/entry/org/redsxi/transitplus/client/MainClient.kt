@file:JvmName("MainClient")

package org.redsxi.transitplus.client

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents
import org.redsxi.mc.ctplus.mapping.Text
import org.redsxi.transitplus.client.network.NetworkLinkClient

fun entry() {
    NetworkLinkClient.init()
    ClientPlayConnectionEvents.JOIN.register { _, _, _ ->
        NetworkLinkClient.initCurrentConnection()
    }
    ClientCommandRegistrationCallback.EVENT.register { dispatcher, _ ->
        dispatcher.register(ClientCommandManager.literal("ctplus").then(ClientCommandManager.literal("ping").executes {
            CoroutineScope(Dispatchers.IO).launch {
                it.source.sendFeedback(Text.literal("Ping success: ${NetworkLinkClient.ping()}MS"))
            }
            0
        }))
    }
}