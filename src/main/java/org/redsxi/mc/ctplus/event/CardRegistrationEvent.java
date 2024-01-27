package org.redsxi.mc.ctplus.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import org.redsxi.mc.ctplus.card.Card;

public interface CardRegistrationEvent {
    void registerCards();

    Event<CardRegistrationEvent> EVENT = EventFactory.createArrayBacked(CardRegistrationEvent.class, list -> () -> {
        for (CardRegistrationEvent e:
             list) {
            e.registerCards();
        }
    });
}
