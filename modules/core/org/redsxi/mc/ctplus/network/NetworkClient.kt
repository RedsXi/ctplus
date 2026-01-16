package org.redsxi.mc.ctplus.network

import net.minecraft.client.Minecraft
import net.minecraft.network.FriendlyByteBuf
import org.redsxi.mc.ctplus.client.ui.RailwayControllerPanelUI

object NetworkClient {
    fun openDashboard(client: Minecraft, buf: FriendlyByteBuf) {
        client.execute {
            if(client.screen !is RailwayControllerPanelUI) {
                client.setScreen(RailwayControllerPanelUI())
            }
        }
    }
}