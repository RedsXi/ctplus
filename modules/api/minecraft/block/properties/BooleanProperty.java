package minecraft.block.properties;

public class BooleanProperty extends Property<Boolean> {
    public BooleanProperty(net.minecraft.world.level.block.state.properties.BooleanProperty o) {
        super(o);
    }

    public static BooleanProperty create(String name) {
        return new BooleanProperty(net.minecraft.world.level.block.state.properties.BooleanProperty.create(name));
    }

    @Override
    public net.minecraft.world.level.block.state.properties.BooleanProperty _get() {
        return (net.minecraft.world.level.block.state.properties.BooleanProperty) _o;
    }
}
