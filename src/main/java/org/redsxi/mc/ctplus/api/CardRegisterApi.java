package org.redsxi.mc.ctplus.api;

import net.minecraft.resources.ResourceLocation;
import org.redsxi.mc.ctplus.Registries;
import org.redsxi.mc.ctplus.card.Card;

public abstract class CardRegisterApi {

    protected Card registerCard(ResourceLocation id, Card card) {
        return Registries.CARD.register(card, id);
    }

    public abstract void registerCards();
}
