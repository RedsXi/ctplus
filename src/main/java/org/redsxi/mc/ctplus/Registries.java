package org.redsxi.mc.ctplus;

import org.redsxi.mc.ctplus.card.Card;
import org.redsxi.mc.ctplus.core.Registry;

public class Registries {
    public static Registry<Card> CARD = new Registry<>(IDKt.getWhiteCard(), CollectionsKt.getCardWhite());
}
