package minecraft.minecraft.core;

public enum Direction {
    DOWN,
    UP,
    NORTH,
    SOUTH,
    WEST,
    EAST;

    public net.minecraft.core.Direction _o() {
        return net.minecraft.core.Direction.values()[ordinal()];
    }

    public static Direction _p(net.minecraft.core.Direction o) {
        return values()[o.ordinal()];
    }
}
