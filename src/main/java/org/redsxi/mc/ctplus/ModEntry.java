package org.redsxi.mc.ctplus;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.impl.itemgroup.ItemGroupHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.entrypoint.EntrypointContainer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.redsxi.mc.ctplus.api.CardRegisterApi;
import org.redsxi.mc.ctplus.card.Card;
import org.redsxi.mc.ctplus.item.ItemCard;
import org.redsxi.mc.ctplus.mapping.RegistryMapper;
import org.redsxi.mc.ctplus.util.ResourceLocationUtil;
import org.redsxi.mc.ctplus.web.HttpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map.Entry;

public class ModEntry implements ModInitializer, ClientModInitializer, DedicatedServerModInitializer {
    private static final Logger LOGGER = LoggerFactory.getLogger("CrabMTR-TransitPlus-Loader");

    public void onInitialize() {
        //System.setProperty("ctplus.transit_plus_svc", "true");
        registerItemGroup(Collections.ItemGroups.MAIN, IDKt.getMain());
        registerItemGroup(Collections.ItemGroups.CARDS, IDKt.getCards());
        registerBlock(Collections.Blocks.TICKET_BARRIER_PAY_DIRECT, IDKt.getTicketBarrierPayDirect(), Collections.ItemGroups.MAIN);
        registerBlock(Collections.Blocks.TICKET_BARRIER_PAY_DIRECT_TP, IDKt.getTicketBarrierPayDirectTp(), Collections.ItemGroups.MAIN);

        registerBlockEntityType(Collections.BlockEntities.TICKET_BARRIER_PAY_DIRECT_TP, IDKt.getTicketBarrierPayDirectTp());

        registerItem(Collections.Items.CT_PLUS, IDKt.getCtPlus(), Collections.ItemGroups.MAIN);

        Registries.CARD.addRegistrationHook(((location, card) -> {

        }));

        registerCard(Collections.Cards.SINGLE_JOURNEY, IDKt.getSingleJourneyCard());

        // For other mods
        for(EntrypointContainer<CardRegisterApi> one : FabricLoader.getInstance().getEntrypointContainers("card_registration", CardRegisterApi.class)) {
            one.getEntrypoint().registerCards();
        }
        LOGGER.info("Registered %d kind(s) of cards".formatted(Registries.CARD.registeredItemCount()));
        Registries.CARD.close();

        for (Entry<ResourceLocation, Card> entry : Registries.CARD) {
            Item item = new ItemCard(entry.getValue());
            Variables.INSTANCE.getCardItemList().put(entry.getValue(), item);
            registerItem(item, ResourceLocationUtil.INSTANCE.addPrefix(entry.getKey(), "card_"), Collections.ItemGroups.CARDS, true);
        }

        //ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(new CardItemTextureLoadListener());
    }

    public void onInitializeClient() {
        //ModelLoadingPlugin.register(new CardModelLoadingPlugin());

        registerBlockCutOutRender(Collections.Blocks.TICKET_BARRIER_PAY_DIRECT);
        registerBlockCutOutRender(Collections.Blocks.TICKET_BARRIER_PAY_DIRECT_TP);
    }

    public void onInitializeServer() {
        HttpService.start();
    }

    private static void registerBlock(Block block, ResourceLocation identifier) {
        LOGGER.info("Registered block %s".formatted(identifier.getPath()));
        RegistryMapper.INSTANCE.registerBlock(identifier, block);
    }

    private static <T extends BlockEntity> void registerBlockEntityType(BlockEntityType<T> block, ResourceLocation identifier) {
        LOGGER.info("Registered block entity %s".formatted(identifier.getPath()));
        RegistryMapper.INSTANCE.registerBlockEntityType(identifier, block);
    }

    private static void registerItem(Item item, ResourceLocation identifier, boolean... dontPrint) {
        RegistryMapper.INSTANCE.registerItem(identifier, item);
        if(dontPrint.length == 0 || dontPrint[0]) return;
        LOGGER.info("Registered item %s".formatted(identifier.getPath()));
    }

    private static void registerBlock(Block block, ResourceLocation identifier, CreativeModeTab group) {
        registerBlock(block, identifier);
        BlockItem bItem = new BlockItem(block, new Item.Properties());
        registerItem(bItem, identifier, group);
    }

    private static void registerItem(Item item, ResourceLocation identifier, CreativeModeTab tab, boolean... dontPrint) {
        registerItem(item, identifier, dontPrint);
        RegistryMapper.INSTANCE.registerItem(item, tab);
    }

    private static void registerCard(Card card, ResourceLocation location) {
        LOGGER.info("Registered card %s".formatted(location.getPath()));
        Registries.CARD.register(card, location);
    }

    private static void registerBlockCutOutRender(Block block) {
        BlockRenderLayerMap.INSTANCE.putBlock(block, RenderType.cutout());
    }

    private void registerItemGroup(CreativeModeTab tab, ResourceLocation id) {
        RegistryMapper.INSTANCE.registerItemGroup(id, tab);
    }


}
