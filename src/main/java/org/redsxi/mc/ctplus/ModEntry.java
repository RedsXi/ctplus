package org.redsxi.mc.ctplus;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.redsxi.mc.ctplus.card.Card;
import org.redsxi.mc.ctplus.event.CardRegistrationEvent;
import org.redsxi.mc.ctplus.mapping.RegistryMapper;
import org.redsxi.mc.ctplus.util.ResourceLocationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map.Entry;

public class ModEntry implements ModInitializer, ClientModInitializer, DedicatedServerModInitializer {
    private static final Logger LOGGER = LoggerFactory.getLogger("CrabMTR-TransitPlus-Loader");

    public void onInitialize() {
       // LOGGER.info("CrabMTR's Extension of MTR version " + VersionConfig.VERSION);
        //System.setProperty("ctplus.transit_plus_svc", "true");

        // registerItemGroup(Collections.ItemGroups.CARDS, IDKt.getCards());

        registerBlock(Collections.Blocks.TICKET_BARRIER_ENTRANCE_TP, IDKt.getTicketBarrierEntranceTp());
        registerBlock(Collections.Blocks.TICKET_BARRIER_EXIT_TP, IDKt.getTicketBarrierExitTp());
        registerBlock(Collections.Blocks.TICKET_BARRIER_PAY_DIRECT, IDKt.getTicketBarrierPayDirect());
        registerBlock(Collections.Blocks.TICKET_BARRIER_PAY_DIRECT_TP, IDKt.getTicketBarrierPayDirectTp());

        registerBlockEntityType(Collections.BlockEntities.TICKET_BARRIER_PAY_DIRECT, IDKt.getTicketBarrierPayDirect());
        registerBlockEntityType(Collections.BlockEntities.TICKET_BARRIER_PAY_DIRECT_TP, IDKt.getTicketBarrierPayDirectTp());

        registerItem(Collections.Items.CT_PLUS, IDKt.getCtPlus());
        registerItem(Collections.Items.TICKET_BARRIER_ENTRANCE_TP, IDKt.getTicketBarrierEntranceTp());
        registerItem(Collections.Items.TICKET_BARRIER_EXIT_TP, IDKt.getTicketBarrierExitTp());
        registerItem(Collections.Items.TICKET_BARRIER_PAY_DIRECT, IDKt.getTicketBarrierPayDirect());
        registerItem(Collections.Items.TICKET_BARRIER_PAY_DIRECT_TP, IDKt.getTicketBarrierPayDirectTp());

        Registries.CARD.addRegistrationHook(((location, card) -> {

        }));

        registerCard(Collections.Cards.PREPAID, IDKt.getPrepaidCard());
        registerCard(Collections.Cards.SINGLE_JOURNEY, IDKt.getSingleJourneyCard());

        // For other mods
        CardRegistrationEvent.EVENT.invoker().registerCards();

        LOGGER.info("Registered %d kind(s) of cards".formatted(Registries.CARD.registeredItemCount()));
        Registries.CARD.close();

        for (Entry<ResourceLocation, Card> entry : Registries.CARD) {
            Item item = entry.getValue().getItem();
            registerItem(item, ResourceLocationUtil.addPrefix(entry.getKey(), "card_"), true);
        }

        registerItemGroup(Collections.ItemGroupBuilders.MAIN.build(), IDKt.getMain());

        ModEntryKt.init();

        //ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(new CardItemTextureLoadListener());
    }

    public void onInitializeClient() {
        //ModelLoadingPlugin.register(new CardModelLoadingPlugin());

        registerBlockCutOutRender(Collections.Blocks.TICKET_BARRIER_PAY_DIRECT);
        registerBlockCutOutRender(Collections.Blocks.TICKET_BARRIER_PAY_DIRECT_TP);

        ModEntryKt.clientInit();


    }

    public void onInitializeServer() {
        // HttpService.start();
        ModEntryKt.serverInit();
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
