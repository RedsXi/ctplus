package org.redsxi.transitplus.server.mixin;

import mtr.data.Rail;
import mtr.data.RailwayData;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(RailwayData.class)
public interface RailwayDataAccessor {
    @Accessor(value = "rails", remap = false)
    Map<BlockPos, Map<BlockPos, Rail>> rails();
}
