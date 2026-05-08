package org.redsxi.transitplus.server

import net.minecraft.resources.ResourceKey
import net.minecraft.server.MinecraftServer
import net.minecraft.world.level.Level
import org.redsxi.transitplus.common.Instance

class ServerInstance(val server: MinecraftServer): Instance {
    override fun getLevel(key: ResourceKey<Level>?)
        = server.getLevel(key!!)!!
}