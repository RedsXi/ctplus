package org.redsxi.mc.ctplus.mixin;

import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.redsxi.mc.ctplus.Variables;
import org.redsxi.mc.ctplus.event.PlayerEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerList.class)
public class PlayerManagerMixin {
    @Inject(at=@At("HEAD"), method = "placeNewPlayer")
    private void onJoin(Connection connection, ServerPlayer player, CallbackInfo ci) {
        PlayerEvent.JOIN.invoker().onPlayerJoin(player);
    }

    @Inject(at=@At("HEAD"), method="remove")
    private void onExit(ServerPlayer player, CallbackInfo ci) {
        PlayerEvent.LEFT.invoker().onPlayerLeft(player);
    }
}
