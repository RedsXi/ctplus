package minecraft.block.properties;

public class Property<T extends Comparable<T>> {


    public static class _FP<U extends Comparable<U>> extends Property<U> {
        public final net.minecraft.world.level.block.state.properties.Property<U> _o;

        public _FP(net.minecraft.world.level.block.state.properties.Property<U> o) {
            _o = o;
        }

        @Override
        public int hashCode() {
            return _o.hashCode();
        }
    }

    public static <V extends Comparable<V>> Property<V> _fp(net.minecraft.world.level.block.state.properties.Property<V> o) {
        return new _FP<>(o);
    }
}
