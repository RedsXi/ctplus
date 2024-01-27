package org.redsxi.mc.ctplus.mapping

import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike
import org.redsxi.mc.ctplus.mapping.Text.ITEM_GROUP
import java.util.ArrayList

object ItemGroupMapper {
    @JvmStatic
    fun create(id: String): CreativeModeTab {
        return CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
            .title(Text.translatable(ITEM_GROUP, id))
            .build()
    }

    @JvmStatic
    fun create(id: String, icon: ItemStack): CreativeModeTab {
        return CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
            .title(Text.translatable(ITEM_GROUP, id))
            .icon{icon}
            .build()
    }

    @JvmStatic
    fun builder(id: String, icon: Item, items: (CreativeModeTab.Output) -> Unit): CreativeModeTab.Builder {
        return CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
            .title(Text.translatable(ITEM_GROUP, id))
            .icon{ItemStack(icon)}
            .displayItems{_, output ->
                items(output)
            }
    }
}