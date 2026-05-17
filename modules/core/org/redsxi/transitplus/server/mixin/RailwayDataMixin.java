package org.redsxi.transitplus.server.mixin;

import mtr.data.RailwayData;
import mtr.data.TransportMode;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.redsxi.transitplus.common.data.rail.Rail;
import org.redsxi.transitplus.server.core.RailwaySystem;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(value = RailwayData.class, remap = false)
public class RailwayDataMixin {
    @Shadow
    @Final
    private Level world;

    @Final
    @Shadow
    private Map<BlockPos, Map<BlockPos, mtr.data.Rail>> rails;

    @Unique
    private ServerLevel serverLevel() {
        if (world.isClientSide) throw new RuntimeException("Current world is not on server side");
        return (ServerLevel) world;
    }

    @Unique
    private RailwaySystem railData() {
        return RailwaySystem.Companion.railwaySystem(serverLevel());
    }

    @Inject(
            at = @At("HEAD"),
            method = "addRail(Lnet/minecraft/world/entity/player/Player;Lmtr/data/TransportMode;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/BlockPos;Lmtr/data/Rail;Z)J",
            remap = false
    )
    private void onAppendRail(
            Player player,
            TransportMode transportMode,
            BlockPos posStart,
            BlockPos posEnd,
            mtr.data.Rail rail0,
            boolean validate,
            CallbackInfoReturnable<Long> cir
    ) {
        if (transportMode != TransportMode.TRAIN) return;
        railData().appendRail(
                posStart, posEnd,
                Rail.Companion.read((RailAccessor) rail0)
        );
    }

    @Inject(
            at = @At("HEAD"),
            method = "removeRailConnection(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/BlockPos;)V",
            remap = false
    )
    private void onRemoveRailConnection(
            Player player,
            BlockPos posStart,
            BlockPos posEnd,
            CallbackInfo ci
    ) {
        railData().removeRail(posStart, posEnd);
    }

    @Inject(
            at = @At("HEAD"),
            method = "removeNode(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/core/BlockPos;Lmtr/data/TransportMode;)V",
            remap = false
    )
    private void onRemoveNode(
            Player player,
            BlockPos nodePos,
            TransportMode transportMode,
            CallbackInfo ci
    ) {
        if (transportMode != TransportMode.TRAIN) return;
        railData().removeNode(nodePos);
    }

    @Inject(
            at = @At("RETURN"),
            method = "load",
            remap = false
    )
    private void onLoaded(
            CompoundTag tag,
            CallbackInfo ci
    ) {
        RailwaySystem.Companion.init(serverLevel(), rails);
    }
}
