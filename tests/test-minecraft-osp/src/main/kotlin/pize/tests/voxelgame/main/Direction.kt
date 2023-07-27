package pize.tests.voxelgame.main

import pize.math.vecmath.vector.Vec2i
import pize.math.vecmath.vector.Vec3i

enum class Direction(x: Int, y: Int, z: Int) {
    NEGATIVE_X(-1, 0, 0),
    POSITIVE_X(1, 0, 0),
    NEGATIVE_Y(0, -1, 0),
    POSITIVE_Y(0, 1, 0),
    NEGATIVE_Z(0, 0, -1),
    POSITIVE_Z(0, 0, 1),
    NONE(0, 0, 0);

    val normal: Vec3i

    init {
        normal = Vec3i(x, y, z)
    }

    companion object {
        fun fromNormal(normal: Vec3i): Direction {
            return fromNormal(normal.x, normal.y, normal.z)
        }

        fun fromNormal(x: Int, y: Int, z: Int): Direction {
            return if (x == 1) POSITIVE_X else if (y == 1) POSITIVE_Y else if (z == 1) POSITIVE_Z else if (x == -1) NEGATIVE_X else if (y == -1) NEGATIVE_Y else if (z == -1) NEGATIVE_Z else NONE
        }

        private val normal2D = arrayOf(
            Vec2i(-1, 0), Vec2i(1, 0), Vec2i(0, -1), Vec2i(0, 1)
        )
        private val normal3D = arrayOf(
            NEGATIVE_X.normal,
            POSITIVE_X.normal,
            NEGATIVE_Y.normal,
            POSITIVE_Y.normal,
            NEGATIVE_Z.normal,
            POSITIVE_Z.normal
        )

        fun normal2DFromIndex(index: Int): Vec2i {
            return normal2D[index]
        }

        fun normal3DFromIndex(index: Int): Vec3i {
            return normal3D[index]
        }
    }
}
