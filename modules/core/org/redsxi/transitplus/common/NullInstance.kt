package org.redsxi.transitplus.common

import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.Level

object NullInstance: Instance {
    override fun getLevel(key: ResourceKey<Level>?): Level = error("Instance is null")
}