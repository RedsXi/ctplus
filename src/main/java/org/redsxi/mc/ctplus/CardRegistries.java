package org.redsxi.mc.ctplus;

import org.redsxi.mc.ctplus.card.Card;
import org.redsxi.mc.ctplus.core.Registry;
import org.redsxi.mc.ctplus.data.CardDataType;

public class CardRegistries {
    /** The registry of cards. You have to register your card into this to use your card correctly. */
    public static final Registry<Card<?, ?>> CARD = new Registry<>(IDKt.getWhiteCard(), Collections.Cards.WHITE_CARD);

    public static final Registry<CardDataType<?>> CARD_DATA_TYPE = new Registry<>(IDKt.getWhiteCard(), Collections.CardDataTypes.WHITE);
}
