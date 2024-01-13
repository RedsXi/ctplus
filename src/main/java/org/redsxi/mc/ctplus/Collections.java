package org.redsxi.mc.ctplus;

import kotlin.reflect.KClass;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.redsxi.mc.ctplus.block.BlockTicketBarrierPayDirect;
import org.redsxi.mc.ctplus.blockentity.BlockEntityTicketBarrierPayDirect;
import org.redsxi.mc.ctplus.card.Card;
import org.redsxi.mc.ctplus.card.SingleJourneyCard;
import org.redsxi.mc.ctplus.card.WhiteCard;
import org.redsxi.mc.ctplus.util.BlockEntityTypeUtil;
import org.redsxi.mc.ctplus.util.ItemGroupUtil;

public interface Collections {
    interface Blocks {
        Block TICKET_BARRIER_PAY_DIRECT = new BlockTicketBarrierPayDirect();
        Block TICKET_BARRIER_PAY_DIRECT_TP = new BlockTicketBarrierPayDirect.WithinTransitPlus();
    }

    interface BlockEntities {
        BlockEntityType<BlockEntityTicketBarrierPayDirect> TICKET_BARRIER_PAY_DIRECT = BlockEntityTypeUtil.INSTANCE.create(Blocks.TICKET_BARRIER_PAY_DIRECT, BlockEntityTicketBarrierPayDirect.class, false);
        BlockEntityType<BlockEntityTicketBarrierPayDirect> TICKET_BARRIER_PAY_DIRECT_TP = BlockEntityTypeUtil.INSTANCE.create(Blocks.TICKET_BARRIER_PAY_DIRECT, BlockEntityTicketBarrierPayDirect.class, true);
    }

    interface Cards {
        Card WHITE_CARD = new WhiteCard();
        Card SINGLE_JOURNEY = new SingleJourneyCard();
    }

    interface ItemGroups {
        CreativeModeTab MAIN = ItemGroupUtil.INSTANCE.create("main");
        CreativeModeTab CARDS = ItemGroupUtil.INSTANCE.create("cards");
    }
}
