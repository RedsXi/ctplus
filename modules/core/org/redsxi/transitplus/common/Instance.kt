package org.redsxi.transitplus.common

import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.Level

interface Instance {
    fun getLevel(key: ResourceKey<Level>? = null): Level
}