package org.redsxi.transitplus.server.mixin;

import mtr.data.Rail;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * MTR中轨道的存储方式是：双向即为两个单向铁轨；将铁轨分成两段，两端分别是直线或圆弧
 */
@Mixin(Rail.class)
public interface RailAccessor {
    // 以下复制来源为MTR 3.x 源码

    // 1. If they are same angle
    // 1. a. If aligned -> Use One Segment
    // 1. b. If not aligned -> Use two Circle, r = (dv^2 + dp^2) / (4dv).
    // 2. If they are right angle -> r = min ( dx,dz ), work around, actually equation 3. can be used.
    // 3. Check if one segment and one circle is available
    // 3. a. If available -> (Segment First) r2 = dv / ( sin(diff) * tan(diff/2) ) = dv / ( 1 - cos(diff)
    // 							for case 2, diff = 90 degrees, r = dv
    //					-> (Circle First) r1 = ( dp - dv / tan(diff) ) / tan (diff/2)
    // 3. b. If not -> r = very complex one. In this case, we need two circles to connect. // 不考虑

    // for curves:
	// x = h + r*cos(T)
	// z = k + r*sin(T)
	// for straight lines (both k and r >= 0.5):
	// x = h*T
	// z = k*T + h*r
	// for straight lines (otherwise):
	// x = h*T + k*r
	// z = k*T + h*r

    /*
    以下
     */

    @Accessor(value = "h1", remap = false)
    double hStart();

    @Accessor(value = "k1", remap = false)
    double kStart();

    @Accessor(value = "r1", remap = false)
    double rStart();

    @Accessor(value = "tStart1", remap = false)
    double tStartStart();

    @Accessor(value = "tEnd1", remap = false)
    double tStartEnd();

    @Accessor(value = "isStraight1", remap = false)
    boolean startStraight();

    @Accessor(value = "reverseT1", remap = false)
    boolean startReverse();


    @Accessor(value = "h1", remap = false)
    double hEnd();

    @Accessor(value = "k1", remap = false)
    double kEnd();

    @Accessor(value = "r1", remap = false)
    double rEnd();

    @Accessor(value = "tStart1", remap = false)
    double tEndStart();

    @Accessor(value = "tEnd1", remap = false)
    double tEndEnd();

    @Accessor(value = "isStraight1", remap = false)
    boolean endStraight();

    @Accessor(value = "reverseT1", remap = false)
    boolean endReverse();
}
