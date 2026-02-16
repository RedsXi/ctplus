package minecraft.block.properties;

public class Property<T extends Comparable<T>> {
    public final net.minecraft.world.level.block.state.properties.Property<T> _o;

    public Property(net.minecraft.world.level.block.state.properties.Property<T> o) {
        _o = o;
    }

    @Override
    public int hashCode() {
        return _o.hashCode();
    }

    public static <V extends Comparable<V>> Property<V> _fp(net.minecraft.world.level.block.state.properties.Property<V> o) {
        return new Property<>(o);
    }

    public net.minecraft.world.level.block.state.properties.Property<T> _get() {
        return _o;
    }
}
