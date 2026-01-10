package minecraft.util;

import org.jetbrains.annotations.NotNull;

public interface StringRepresentable {
    String getName();

    class _FP implements StringRepresentable {
        private final net.minecraft.util.StringRepresentable _o;

        _FP(net.minecraft.util.StringRepresentable o) {
            _o = o;
        }

        @Override
        public String getName() {
            return _o.getSerializedName();
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }
    }

    class _RP implements net.minecraft.util.StringRepresentable {
        private final StringRepresentable _p;

        _RP(StringRepresentable p) {
            _p = p;
        }

        @Override
        public String getSerializedName() {
            return _p.getName();
        }

        @Override
        public int hashCode() {
            return _p.hashCode();
        }
    }

    static StringRepresentable _fp(net.minecraft.util.StringRepresentable o) {
        return new _FP(o);
    }

    static net.minecraft.util.StringRepresentable _rp(StringRepresentable p) {
        return new _RP(p);
    }
}
