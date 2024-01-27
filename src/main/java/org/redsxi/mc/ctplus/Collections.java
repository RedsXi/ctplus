package org.redsxi.mc.ctplus;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.GameRules.Key;
import net.minecraft.world.level.GameRules.IntegerValue;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.properties.Property;
import org.redsxi.mc.ctplus.block.BlockTicketBarrierPayDirect;
import org.redsxi.mc.ctplus.blockentity.JBlockEntityTicketBarrierPayDirect;
import org.redsxi.mc.ctplus.card.Card;
import org.redsxi.mc.ctplus.card.PrepaidCard;
import org.redsxi.mc.ctplus.card.SingleJourneyCard;
import org.redsxi.mc.ctplus.card.WhiteCard;
import org.redsxi.mc.ctplus.util.BlockEntityTypeUtil;
import org.redsxi.mc.ctplus.mapping.ItemGroupMapper;

public interface Collections {
    interface Blocks {
        Block TICKET_BARRIER_PAY_DIRECT = new BlockTicketBarrierPayDirect();
        Block TICKET_BARRIER_PAY_DIRECT_TP = new BlockTicketBarrierPayDirect.WithinTransitPlus();
    }

    interface BlockEntities {
        BlockEntityType<JBlockEntityTicketBarrierPayDirect> TICKET_BARRIER_PAY_DIRECT = BlockEntityTypeUtil.INSTANCE.create(Blocks.TICKET_BARRIER_PAY_DIRECT, JBlockEntityTicketBarrierPayDirect.class, false);
        BlockEntityType<JBlockEntityTicketBarrierPayDirect> TICKET_BARRIER_PAY_DIRECT_TP = BlockEntityTypeUtil.INSTANCE.create(Blocks.TICKET_BARRIER_PAY_DIRECT, JBlockEntityTicketBarrierPayDirect.class, true);
    }

    interface Cards {
        Card PREPAID = new PrepaidCard();
        Card SINGLE_JOURNEY = new SingleJourneyCard();
        Card WHITE_CARD = new WhiteCard();
    }

    interface Rules {
        Key<IntegerValue> ENGLISH_TRANSLATE_INDEX = GameRuleRegistry.register("mtr_translate_index_en", GameRules.Category.PLAYER, GameRuleFactory.createIntRule(1, 0));
        Key<IntegerValue> CHINESE_TRANSLATE_INDEX = GameRuleRegistry.register("mtr_translate_index_zh", GameRules.Category.PLAYER, GameRuleFactory.createIntRule(0, 0));
    }

    interface Items {
        private static Item createBlockItem(Block block) {
            return new BlockItem(block, new Item.Properties());
        }

        static ItemStack asStack(Item item) {
            return new ItemStack(item, 1);
        }
        Item CT_PLUS = new Item(new Item.Properties());

        Item TICKET_BARRIER_PAY_DIRECT = createBlockItem(Blocks.TICKET_BARRIER_PAY_DIRECT);
        Item TICKET_BARRIER_PAY_DIRECT_TP = createBlockItem(Blocks.TICKET_BARRIER_PAY_DIRECT_TP);
    }

    interface ItemGroupBuilders {
        CreativeModeTab.Builder MAIN = ItemGroupMapper.builder("main", Items.CT_PLUS, out -> {
            out.accept(Items.asStack(Items.CT_PLUS));
            out.accept(Items.asStack(Items.TICKET_BARRIER_PAY_DIRECT));
            out.accept(Items.asStack(Items.TICKET_BARRIER_PAY_DIRECT_TP));
            return Unit.INSTANCE;
        });
        //CreativeModeTab CARDS = ItemGroupMapper.INSTANCE.create("cards");
    }
}
