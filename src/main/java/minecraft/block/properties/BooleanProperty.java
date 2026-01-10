package minecraft.block.properties;

public class BooleanProperty extends Property<Boolean> {
    public static class _FP extends BooleanProperty {
        public final net.minecraft.world.level.block.state.properties.BooleanProperty _o;

        public _FP(net.minecraft.world.level.block.state.properties.BooleanProperty o) {
            _o = o;
        }
    }

    public static BooleanProperty create(String name) {
        return new _FP(net.minecraft.world.level.block.state.properties.BooleanProperty.create(name));
    }
}
