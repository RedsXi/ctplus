package org.redsxi.transitplus.server.mixin;

import mtr.data.*;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.redsxi.transitplus.common.data.rail.Rail;
import org.redsxi.transitplus.common.logger.LoggerKt;
import org.redsxi.transitplus.server.MixinsKt;
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

    @Unique
    private static RailwaySystem getRailwaySystem(Level world) {
        if (world.isClientSide) throw new RuntimeException("Unexcepted client level type");
        return RailwaySystem.Companion.railwaySystem((ServerLevel) world);
    }

    @Final
    @Shadow
    private Level world;

    @Inject(at = @At("HEAD"), method = "addRail(Lnet/minecraft/world/entity/player/Player;Lmtr/data/TransportMode;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/BlockPos;Lmtr/data/Rail;Z)J")
    private void onAppendRail(
            Player player,
            TransportMode transportMode,
            BlockPos posStart,
            BlockPos posEnd,
            mtr.data.Rail rail,
            boolean validate,
            CallbackInfoReturnable<Long> cir
    ) {
        if (transportMode != TransportMode.TRAIN) return;
        getRailwaySystem(world).appendRail(posStart, posEnd, Rail.Companion.read(MixinsKt.accessor(rail)));
    }

    @Inject(at = @At("HEAD"), method = "removeRailConnection(Lnet/minecraft/world/level/Level;Ljava/util/Map;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/BlockPos;)V")
    private static void onRemoveRail(
            Level world,
            Map<BlockPos, Map<BlockPos, mtr.data.Rail>> rails,
            BlockPos pos1,
            BlockPos pos2,
            CallbackInfo ci
    ) {
        getRailwaySystem(world).removeRail(pos1, pos2);
    }

    @Inject(at = @At("HEAD"), method = "removeNode(Lnet/minecraft/world/level/Level;Ljava/util/Map;Lnet/minecraft/core/BlockPos;)V")
    private static void onRemoveNode(
            Level world,
            Map<BlockPos, Map<BlockPos, mtr.data.Rail>> rails,
            BlockPos pos,
            CallbackInfo ci
    ) {
        getRailwaySystem(world).removeNode(pos);
    }
}
