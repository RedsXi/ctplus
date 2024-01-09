package org.redsxi.mc.crabgc;

import it.unimi.dsi.fastutil.booleans.BooleanBooleanPair;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;

public class Properties {
    public static final Property<Boolean> OPEN = BooleanProperty.create("open");
    public static final Property<Direction> HORIZONTAL_FACING = BlockStateProperties.HORIZONTAL_FACING;
}
