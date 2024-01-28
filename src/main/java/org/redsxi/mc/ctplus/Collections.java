package org.redsxi.mc.ctplus;

import kotlin.Unit;
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
import org.redsxi.bool.False;
import org.redsxi.bool.True;
import org.redsxi.mc.ctplus.block.OldBlockTicketBarrierPayDirect;
import org.redsxi.mc.ctplus.blockentity.BlockEntityTicketBarrierPayDirect;
import org.redsxi.mc.ctplus.card.Card;
import org.redsxi.mc.ctplus.card.PrepaidCard;
import org.redsxi.mc.ctplus.card.SingleJourneyCard;
import org.redsxi.mc.ctplus.card.WhiteCard;
import org.redsxi.mc.ctplus.util.BlockEntityTypeUtil;
import org.redsxi.mc.ctplus.mapping.ItemGroupMapper;

public interface Collections {
    interface Blocks {
        Block TICKET_BARRIER_PAY_DIRECT = new OldBlockTicketBarrierPayDirect();
        Block TICKET_BARRIER_PAY_DIRECT_TP = new OldBlockTicketBarrierPayDirect.WithinTransitPlus();
    }

    interface BlockEntities {
        BlockEntityType<BlockEntityTicketBarrierPayDirect> TICKET_BARRIER_PAY_DIRECT = BlockEntityTypeUtil.INSTANCE.create(Blocks.TICKET_BARRIER_PAY_DIRECT, BlockEntityTicketBarrierPayDirect.class, False.INSTANCE);
        BlockEntityType<BlockEntityTicketBarrierPayDirect> TICKET_BARRIER_PAY_DIRECT_TP = BlockEntityTypeUtil.INSTANCE.create(Blocks.TICKET_BARRIER_PAY_DIRECT, BlockEntityTicketBarrierPayDirect.class, True.INSTANCE);
    }

    interface Cards {
        Card PREPAID = new PrepaidCard();
        Card SINGLE_JOURNEY = new SingleJourneyCard();
        Card WHITE_CARD = new WhiteCard();
    }

    interface Rules {
        Key<IntegerValue> TRANSLATE_INDEX = GameRuleRegistry.register("mtr_translate_index", GameRules.Category.PLAYER, GameRuleFactory.createIntRule(1, 0));
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
