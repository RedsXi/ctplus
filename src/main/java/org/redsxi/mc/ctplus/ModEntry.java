package org.redsxi.mc.ctplus;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.redsxi.mc.ctplus.api.CardRegisterApi;
import org.redsxi.mc.ctplus.card.Card;
import org.redsxi.mc.ctplus.command.CommandStructures;
import org.redsxi.mc.ctplus.data.CardDataType;
import org.redsxi.mc.ctplus.generated.BuildProps;
import org.redsxi.mc.ctplus.generated.RuntimeVariables;
import org.redsxi.mc.ctplus.mapping.RegistryMapper;
import org.redsxi.mc.ctplus.network.SetTranslationIndexS2CPacket;
import org.redsxi.mc.ctplus.util.Date;
import org.redsxi.mc.ctplus.util.ResourceLocationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map.Entry;

public class ModEntry implements ModInitializer, ClientModInitializer, DedicatedServerModInitializer {
    private static final Logger LOGGER = LoggerFactory.getLogger("CrabMTR-TransitPlus-Loader");

    public void onInitialize() {
        LOGGER.info("CrabMTR Transit+ version " + BuildProps.VERSION);
        if(RuntimeVariables.DEBUG) {
            LOGGER.info("Build time " + Date.get(BuildProps.BUILD_TIME));
            LOGGER.warn("You're running a debug version of CTPlus!");
        }
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

        CTPlusRegistries.CARD.addRegistrationHook(((location, card) -> {

        }));

        registerCard(Collections.Cards.PREPAID, Collections.CardDataTypes.PREPAID, IDKt.getPrepaidCard());
        registerCard(Collections.Cards.SINGLE_JOURNEY, Collections.CardDataTypes.SINGLE_JOURNEY, IDKt.getSingleJourneyCard());



        // For other mods
        FabricLoader.getInstance().getEntrypointContainers("card-reg", CardRegisterApi.class).forEach(container -> container.getEntrypoint().registerCards());

        LOGGER.info("Registered %d kind(s) of cards".formatted(CTPlusRegistries.CARD.registeredItemCount()));
        CTPlusRegistries.CARD.close();

        for (Entry<ResourceLocation, Card<?, ?>> entry : CTPlusRegistries.CARD) {
            Card<?, ?> card = entry.getValue();
            card.initItem();
            Item item = entry.getValue().getItem();
            registerItem(item, ResourceLocationUtil.addPrefix(entry.getKey(), "card_"), true);
        }

        registerItemGroup(Collections.ItemGroupBuilders.MAIN.build(), IDKt.getMain());

        registerCommand(CommandStructures.CTPLUS);
        registerCommand(CommandStructures.SET_PASS_COST);
        registerCommand(CommandStructures.GET_CARD);
        registerCommand(CommandStructures.VARIABLES);

        ClientPlayNetworking.registerGlobalReceiver(SetTranslationIndexS2CPacket.TYPE, (packet, u, v) -> Variables.INSTANCE.setTranslationIndex(packet.getIndex()));
    }

    public void onInitializeClient() {
        LOGGER.info("Environment: Client & Integrated server");
        registerBlockCutOutRender(Collections.Blocks.TICKET_BARRIER_PAY_DIRECT);
        registerBlockCutOutRender(Collections.Blocks.TICKET_BARRIER_PAY_DIRECT_TP);
    }

    public void onInitializeServer() {
        LOGGER.info("Environment: Dedicated server");
    }

    private static void registerBlock(Block block, ResourceLocation identifier) {
        LOGGER.debug("Registered block %s".formatted(identifier.getPath()));
        RegistryMapper.INSTANCE.registerBlock(identifier, block);
    }

    private static <T extends BlockEntity> void registerBlockEntityType(BlockEntityType<T> block, ResourceLocation identifier) {
        LOGGER.debug("Registered block entity %s".formatted(identifier.getPath()));
        RegistryMapper.INSTANCE.registerBlockEntityType(identifier, block);
    }

    private static void registerItem(Item item, ResourceLocation identifier, boolean... dontPrint) {
        RegistryMapper.INSTANCE.registerItem(identifier, item);
        if(dontPrint.length == 0 || dontPrint[0]) return;
        LOGGER.debug("Registered item %s".formatted(identifier.getPath()));
    }
    private static void registerCard(Card<?, ?> card, CardDataType<?> dataType, ResourceLocation location) {
        LOGGER.debug("Registered card %s".formatted(location.getPath()));
        CTPlusRegistries.CARD_DATA_TYPE.register(dataType, location);
        CTPlusRegistries.CARD.register(card, location);
    }

    private static void registerBlockCutOutRender(Block block) {
        BlockRenderLayerMap.INSTANCE.putBlock(block, RenderType.cutout());
    }

    private void registerItemGroup(CreativeModeTab tab, ResourceLocation id) {
        RegistryMapper.INSTANCE.registerItemGroup(id, tab);
    }

    private void registerCommand(LiteralArgumentBuilder<CommandSourceStack> command) {
        CommandRegistrationCallback.EVENT.register((dispatcher, u1, u2) -> dispatcher.register(command));
    }
}
