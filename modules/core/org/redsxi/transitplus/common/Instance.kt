package org.redsxi.transitplus.common

import net.minecraft.client.Minecraft
import net.minecraft.resources.ResourceKey
import net.minecraft.server.MinecraftServer
import net.minecraft.world.level.Level
import org.redsxi.transitplus.client.ClientInstance
import org.redsxi.transitplus.server.ServerInstance

interface Instance {
    fun getLevel(key: ResourceKey<Level>? = null): Level

    companion object {
        fun Minecraft.instance() = ClientInstance
        fun MinecraftServer.instance() = ServerInstance(this)
    }
}