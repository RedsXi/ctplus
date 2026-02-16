package minecraft.block.properties;

import net.minecraft.util.StringRepresentable;

public class EnumProperty<E extends Enum<E> & StringRepresentable> extends Property<E> {
    public EnumProperty(net.minecraft.world.level.block.state.properties.EnumProperty<E> o) {
        super(o);
    }

    public static BooleanProperty create(String name) {
        return new BooleanProperty(net.minecraft.world.level.block.state.properties.BooleanProperty.create(name));
    }

    @Override
    public net.minecraft.world.level.block.state.properties.EnumProperty<E> _get() {
        return (net.minecraft.world.level.block.state.properties.EnumProperty<E>) _o;
    }


}
