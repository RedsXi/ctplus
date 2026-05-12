package org.redsxi.transitplus.client

import net.minecraft.client.Minecraft
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.Level
import org.redsxi.transitplus.common.Instance

object ClientInstance: Instance {
    override fun getLevel(key: ResourceKey<Level>?) = Minecraft.getInstance().level!!
}