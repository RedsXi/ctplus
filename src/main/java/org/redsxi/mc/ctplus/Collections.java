package org.redsxi.mc.ctplus;

import kotlin.Unit;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.redsxi.bool.Bool;
import org.redsxi.mc.ctplus.block.BlockTicketBarrierEntranceTP;
import org.redsxi.mc.ctplus.block.BlockTicketBarrierExitTP;
import org.redsxi.mc.ctplus.block.BlockTicketBarrierPayDirect;
import org.redsxi.mc.ctplus.block.BlockTicketBarrierPayDirectTP;
import org.redsxi.mc.ctplus.blockentity.BlockEntityTicketBarrierPayDirect;
import org.redsxi.mc.ctplus.card.Card;
import org.redsxi.mc.ctplus.card.PrepaidCard;
import org.redsxi.mc.ctplus.card.SingleJourneyCard;
import org.redsxi.mc.ctplus.card.WhiteCard;
import org.redsxi.mc.ctplus.data.CardData;
import org.redsxi.mc.ctplus.data.CardDataType;
import org.redsxi.mc.ctplus.data.PrepaidCardData;
import org.redsxi.mc.ctplus.data.SingleJourneyCardData;
import org.redsxi.mc.ctplus.util.BlockEntityTypeUtil;
import org.redsxi.mc.ctplus.mapping.ItemGroupMapper;

public interface Collections {
    interface Blocks {
        Block TICKET_BARRIER_ENTRANCE_TP = new BlockTicketBarrierEntranceTP();
        Block TICKET_BARRIER_EXIT_TP = new BlockTicketBarrierExitTP();
        Block TICKET_BARRIER_PAY_DIRECT = new BlockTicketBarrierPayDirect();
        Block TICKET_BARRIER_PAY_DIRECT_TP = new BlockTicketBarrierPayDirectTP();
    }

    interface BlockEntities {
        BlockEntityType<BlockEntityTicketBarrierPayDirect> TICKET_BARRIER_PAY_DIRECT = BlockEntityTypeUtil.INSTANCE.create(Blocks.TICKET_BARRIER_PAY_DIRECT, BlockEntityTicketBarrierPayDirect.class, Bool.FALSE);
        BlockEntityType<BlockEntityTicketBarrierPayDirect> TICKET_BARRIER_PAY_DIRECT_TP = BlockEntityTypeUtil.INSTANCE.create(Blocks.TICKET_BARRIER_PAY_DIRECT, BlockEntityTicketBarrierPayDirect.class, Bool.TRUE);
    }

    interface Cards {
        PrepaidCard PREPAID = new PrepaidCard();
        SingleJourneyCard SINGLE_JOURNEY = new SingleJourneyCard();
        WhiteCard WHITE_CARD = new WhiteCard();
    }

    interface CardDataTypes {
        CardDataType<CardData> WHITE = CardDataType.createSimple(CardData::new);
        CardDataType<SingleJourneyCardData> SINGLE_JOURNEY = CardDataType.createSimple(SingleJourneyCardData::new);
        CardDataType<PrepaidCardData> PREPAID = CardDataType.createSimple(PrepaidCardData::new);
    }

    interface Items {
        private static Item createBlockItem(Block block) {
            return new BlockItem(block, new Item.Properties());
        }

        static ItemStack asStack(Item item) {
            return new ItemStack(item, 1);
        }
        Item CT_PLUS = new Item(new Item.Properties());
        Item TICKET_BARRIER_ENTRANCE_TP = createBlockItem(Blocks.TICKET_BARRIER_ENTRANCE_TP);
        Item TICKET_BARRIER_EXIT_TP = createBlockItem(Blocks.TICKET_BARRIER_EXIT_TP);
        Item TICKET_BARRIER_PAY_DIRECT = createBlockItem(Blocks.TICKET_BARRIER_PAY_DIRECT);
        Item TICKET_BARRIER_PAY_DIRECT_TP = createBlockItem(Blocks.TICKET_BARRIER_PAY_DIRECT_TP);
    }

    interface ItemGroupBuilders {
        CreativeModeTab.Builder MAIN = ItemGroupMapper.builder("main", Items.CT_PLUS, out -> {
            out.accept(Items.asStack(Items.CT_PLUS));
            out.accept(Items.asStack(Items.TICKET_BARRIER_ENTRANCE_TP));
            out.accept(Items.asStack(Items.TICKET_BARRIER_EXIT_TP));
            out.accept(Items.asStack(Items.TICKET_BARRIER_PAY_DIRECT));
            out.accept(Items.asStack(Items.TICKET_BARRIER_PAY_DIRECT_TP));
            return Unit.INSTANCE;
        });
        //CreativeModeTab CARDS = ItemGroupMapper.INSTANCE.create("cards");
    }
}
