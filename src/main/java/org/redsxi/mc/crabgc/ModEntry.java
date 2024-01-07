package org.redsxi.mc.crabgc;

import net.fabricmc.api.ModInitializer;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEntry implements ModInitializer {
    public void onInitialize() {
        registerBlock(CollectionsKt.getBlockTicketBarrierPayDirect(), IDKt.getTicketBarrierPayDirect());
    }


    private static void registerBlock(Block block, Identifier identifier) {
        Registry.register(
                Registries.BLOCK,
                identifier,
                block
        );
    }

    private static void registerItem(Item item, Identifier identifier) {
        Registry.register(
                Registries.ITEM,
                identifier,
                item
        );
    }

    private static void registerBlock(Block block, Identifier identifier, ItemGroup group) {
        registerBlock(block, identifier);

    }
}
