package org.redsxi.mc.ctplus;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.redsxi.mc.ctplus.block.BlockTicketBarrierPayDirect;
import org.redsxi.mc.ctplus.blockentity.JBlockEntityTicketBarrierPayDirect;
import org.redsxi.mc.ctplus.card.Card;
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
        Card WHITE_CARD = new WhiteCard();
        Card SINGLE_JOURNEY = new SingleJourneyCard();
    }

    interface Items {
        Item CT_PLUS = new Item(new Item.Properties());
    }

    interface ItemGroups {
        CreativeModeTab MAIN = ItemGroupMapper.INSTANCE.create("main", new ItemStack(Items.CT_PLUS));
        CreativeModeTab CARDS = ItemGroupMapper.INSTANCE.create("cards");
    }
}
