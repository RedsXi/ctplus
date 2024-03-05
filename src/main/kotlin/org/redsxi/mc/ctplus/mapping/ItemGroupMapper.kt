package org.redsxi.mc.ctplus.mapping

import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import org.redsxi.mc.ctplus.mapping.Text.ITEM_GROUP

object ItemGroupMapper {
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