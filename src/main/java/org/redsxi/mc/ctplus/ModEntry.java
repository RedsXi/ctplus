package org.redsxi.mc.ctplus;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.impl.itemgroup.ItemGroupHelper;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.redsxi.mc.ctplus.web.HttpService;

public class ModEntry implements ModInitializer, ClientModInitializer, DedicatedServerModInitializer {
    public void onInitialize() {
        System.setProperty("ctplus.transit_plus_svc", "true");
        registerItemGroup(CollectionsKt.getItemGroupMain());
        registerBlock(CollectionsKt.getBlockTicketBarrierPayDirect(), IDKt.getTicketBarrierPayDirect(), CollectionsKt.getItemGroupMain());
        registerItem(CollectionsKt.getItemCard(), IDKt.getCard(), CollectionsKt.getItemGroupMain());
    }

    public void onInitializeClient() {
        registerBlockCutOutRender(CollectionsKt.getBlockTicketBarrierPayDirect());
    }

    public void onInitializeServer() {
        HttpService.start();
    }

    private static void registerBlock(Block block, ResourceLocation identifier) {
        Registry.register (
                BuiltInRegistries.BLOCK,
                identifier,
                block
        );
    }

    private static void registerItem(Item item, ResourceLocation identifier) {
        Registry.register (
                BuiltInRegistries.ITEM,
                identifier,
                item
        );
    }

    private static void registerBlock(Block block, ResourceLocation identifier, CreativeModeTab group) {
        registerBlock(block, identifier);
        BlockItem bItem = new BlockItem(block, new Item.Properties());
        registerItem(bItem, identifier);
        ItemGroupEvents.modifyEntriesEvent(group).register((entries) -> {
            entries.accept(bItem);
        });
    }

    private static void registerItem(Item item, ResourceLocation identifier, CreativeModeTab tab) {
        registerItem(item, identifier);
        ItemGroupEvents.modifyEntriesEvent(tab).register((entries) -> {
            entries.accept(item);
        });
    }

    private static void registerBlockCutOutRender(Block block) {
        BlockRenderLayerMap.INSTANCE.putBlock(block, RenderType.cutout());
    }

    private void registerItemGroup(CreativeModeTab tab) {
        ItemGroupHelper.appendItemGroup(tab);
    }
}
