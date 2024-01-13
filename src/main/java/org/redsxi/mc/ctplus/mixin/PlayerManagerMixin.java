package org.redsxi.mc.ctplus.mixin;

import net.minecraft.network.Connection;
import net.minecraft.server.commands.GiveCommand;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.redsxi.mc.ctplus.Variables;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(PlayerList.class)
public class PlayerManagerMixin {
    @Inject(at=@At("HEAD"), method = "placeNewPlayer")
    private void onJoin(Connection connection, ServerPlayer player, CallbackInfo ci) {
        Variables.INSTANCE.getPlayerList().put(player.getUUID(), player);
    }

    @Inject(at=@At("HEAD"), method="remove")
    private void onExit(ServerPlayer player, CallbackInfo ci) {
        Variables.INSTANCE.getPlayerList().remove(player.getUUID());
    }
}
