package org.redsxi.mc.ctplus;

import minecraft.block.properties.BooleanProperty;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;

public class Properties {
    public static final Property<Boolean> OPEN = ((BooleanProperty._FP)BooleanProperty.create("open"))._o;
    public static final Property<Direction> HORIZONTAL_FACING = BlockStateProperties.HORIZONTAL_FACING;
}
