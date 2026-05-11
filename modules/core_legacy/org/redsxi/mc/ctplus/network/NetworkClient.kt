package org.redsxi.mc.ctplus.network

import net.minecraft.client.Minecraft
import net.minecraft.network.FriendlyByteBuf
import org.redsxi.transitplus.client.ui.RcpScreen

object NetworkClient {
    fun openDashboard(client: Minecraft, buf: FriendlyByteBuf) {
        client.execute {
            if(client.screen !is RcpScreen) {
                client.setScreen(RcpScreen())
            }
        }
    }
}