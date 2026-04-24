package org.redsxi.transitplus.common.data.rail

import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.BlockPos
import org.redsxi.transitplus.server.mixin.RailAccessor

class Rail(
    val start: RailSegment,
    val end: RailSegment,
    var direction: Direction = Direction.FORWARD,
) {
    companion object {
        val CODEC: Codec<Rail> = RecordCodecBuilder.create { builder ->
            builder.group(
                RailSegment.CODEC.fieldOf("Start").forGetter{it.start},
                RailSegment.CODEC.fieldOf("End").forGetter{it.end},
                Direction.CODEC.fieldOf("Direction").forGetter{it.direction}
            ).apply(builder) { start, end, direction ->
                Rail(start, end, direction)
            }
        }

        val LONG_HEX_FORMAT = HexFormat {
            upperCase = true
            number {
                removeLeadingZeros = true
                minLength = 16
            }
        }

        val BLOCK_POS_PAIR_CODEC: Codec<Pair<BlockPos, BlockPos>> = Codec.STRING.comapFlatMap(
            { str ->
                try {
                    DataResult.success(Pair(
                        BlockPos.of(str.substring(0, 16).toLong(16)),
                        BlockPos.of(str.substring(17, 32).toLong(16))
                    ))
                } catch (e: Exception) {
                    DataResult.error(e.toString())
                }
            },
            { pair ->
                "%016X%016X".format(pair.first.asLong(), pair.second.asLong())
            }
        )

        val MAP_POSITIONED_RAIL_CODEC: Codec<MutableMap<Pair<BlockPos, BlockPos>, Rail>> = Codec.unboundedMap(BLOCK_POS_PAIR_CODEC, CODEC)

        fun read(
            h1: Double,
            k1: Double,
            r1: Double,
            tStart1: Double,
            tEnd1: Double,
            straight1: Boolean,
            h2: Double,
            k2: Double,
            r2: Double,
            tStart2: Double,
            tEnd2: Double,
            straight2: Boolean,
            rev1: Boolean,
            rev2: Boolean,
            direction: Int = 0
        ) = Rail(
            RailSegment.read(h1, k1, r1, tStart1, tEnd1, straight1, rev1),
            RailSegment.read(h2, k2, r2, tStart2, tEnd2, straight2, rev2),
            Direction.fromId(direction)
        )

        /**
         * @param rail 假设r0是MTR的Rail: ```r0.accessor()```
         */
        fun read(rail: RailAccessor): Rail {
            return read(
                rail.hStart(),
                rail.kStart(),
                rail.rStart(),
                rail.tStartStart(),
                rail.tStartEnd(),
                rail.startStraight(),
                rail.hEnd(),
                rail.kEnd(),
                rail.rEnd(),
                rail.tEndStart(),
                rail.tEndEnd(),
                rail.endStraight(),
                rail.startReverse(),
                rail.endReverse()
            )
        }
    }

    fun forward() {
        direction = direction.and(Direction.FORWARD)
    }

    fun backward() {
        direction = direction.and(Direction.BACKWARD)
    }

    enum class Direction {
        FORWARD,
        BACKWARD,
        BOTH,
        NONE;

        fun id() = when(this) {
            FORWARD -> 1
            BACKWARD -> 2
            BOTH -> 3
            NONE -> 0
        }

        fun and(another: Direction): Direction {
            return when(this) {
                FORWARD -> when(another) {
                        FORWARD -> FORWARD
                        BACKWARD -> BOTH
                        BOTH -> BOTH
                        NONE -> FORWARD
                    }
                BACKWARD -> when(another) {
                        FORWARD -> BOTH
                        BACKWARD -> BACKWARD
                        BOTH -> BOTH
                        NONE -> BACKWARD
                    }
                BOTH -> BOTH
                NONE -> when(another) {
                    FORWARD -> FORWARD
                    BACKWARD -> BACKWARD
                    BOTH -> BOTH
                    NONE -> NONE
                }
            }
        }

        companion object {
            fun fromId(id: Int) = when(id) {
                0 -> NONE
                1 -> FORWARD
                2 -> BACKWARD
                3 -> BOTH
                else -> throw IllegalArgumentException()
            }

            val CODEC: Codec<Direction> = Codec.INT.xmap({fromId(it)}, {it.id()})
        }
    }
}