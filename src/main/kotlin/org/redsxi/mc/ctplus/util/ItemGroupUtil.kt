package org.redsxi.mc.ctplus.util

import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.ItemStack
import org.redsxi.mc.ctplus.mapping.Text

object ItemGroupUtil {
    fun create(id: String): CreativeModeTab {
        return CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
            .title(Text.itemGroup(id))
            .build()
    }

    fun create(id: String, icon: ItemStack): CreativeModeTab {
        return CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
            .title(Text.itemGroup(id))
            .icon{icon}
            .build()
    }
}