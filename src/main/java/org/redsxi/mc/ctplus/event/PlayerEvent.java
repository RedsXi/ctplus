package org.redsxi.mc.ctplus.event;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.level.ServerPlayer;

@Environment(EnvType.SERVER)
public final class PlayerEvent {
    public interface Join {
        void onPlayerJoin(ServerPlayer player);
    }

    public interface Left {
        void onPlayerLeft(ServerPlayer player);
    }

    public static final Event<Join> JOIN = EventFactory.createArrayBacked(Join.class, listeners -> player -> {
        for(Join j: listeners) j.onPlayerJoin(player);
    });

    public static final Event<Left> LEFT = EventFactory.createArrayBacked(Left.class, listeners -> player -> {
        for(Left l: listeners) l.onPlayerLeft(player);
    });
}
