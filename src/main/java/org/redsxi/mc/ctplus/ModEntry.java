package org.redsxi.mc.ctplus;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.impl.itemgroup.ItemGroupHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.entrypoint.EntrypointContainer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.redsxi.mc.ctplus.api.CardRegisterApi;
import org.redsxi.mc.ctplus.card.Card;
//import org.redsxi.mc.ctplus.resource.CardItemTextureLoadListener;
import org.redsxi.mc.ctplus.item.ItemCard;
import org.redsxi.mc.ctplus.model.CardModelLoadingPlugin;
import org.redsxi.mc.ctplus.web.HttpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map.Entry;

public class ModEntry implements ModInitializer, ClientModInitializer, DedicatedServerModInitializer {
    private static final Logger LOGGER = LoggerFactory.getLogger("CrabMTR-TransitPlus-Loader");

    public void onInitialize() {
        System.setProperty("ctplus.transit_plus_svc", "true");
        registerItemGroup(CollectionsKt.getItemGroupMain());
        registerBlock(CollectionsKt.getBlockTicketBarrierPayDirect(), IDKt.getTicketBarrierPayDirect(), CollectionsKt.getItemGroupMain());
        registerItem(CollectionsKt.getItemCard(), IDKt.getCard(), CollectionsKt.getItemGroupMain());

        Registries.CARD.addRegistrationHook(((location, card) -> {
            Item item = new ItemCard(card);
            Variables.INSTANCE.getCardItemList().put(card, item);
            registerItem(item, location.withPrefix("card_"));
        }));

        // TODO: Complete this shit card
        // No cards to register now (will have some)

        // For other mods
        for(EntrypointContainer<CardRegisterApi> one : FabricLoader.getInstance().getEntrypointContainers("card_registration", CardRegisterApi.class)) {
            one.getEntrypoint().registerCards();
        }
        LOGGER.info("Registered %d kind(s) of cards".formatted(Registries.CARD.registeredItemCount()));
        Registries.CARD.close();

        //ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(new CardItemTextureLoadListener());
    }

    public void onInitializeClient() {
        registerBlockCutOutRender(CollectionsKt.getBlockTicketBarrierPayDirect());

        ModelLoadingPlugin.register(new CardModelLoadingPlugin());
    }

    public void onInitializeServer() {
        HttpService.start();
    }

    private static void registerBlock(Block block, ResourceLocation identifier) {
        LOGGER.info("Registered block %s".formatted(identifier.getPath()));
        Registry.register (
                BuiltInRegistries.BLOCK,
                identifier,
                block
        );
    }

    private static void registerItem(Item item, ResourceLocation identifier, boolean... dontPrint) {
        Registry.register (
                BuiltInRegistries.ITEM,
                identifier,
                item
        );
        if(dontPrint.length == 0 || dontPrint[0]) return;
        LOGGER.info("Registered item %s".formatted(identifier.getPath()));
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

    private static void registerCard(Card card, ResourceLocation location) {
        LOGGER.info("Registered card %s".formatted(location.getPath()));
        Registries.CARD.register(card, location);
    }

    private static void registerBlockCutOutRender(Block block) {
        BlockRenderLayerMap.INSTANCE.putBlock(block, RenderType.cutout());
    }

    private void registerItemGroup(CreativeModeTab tab) {
        ItemGroupHelper.appendItemGroup(tab);
    }


}
