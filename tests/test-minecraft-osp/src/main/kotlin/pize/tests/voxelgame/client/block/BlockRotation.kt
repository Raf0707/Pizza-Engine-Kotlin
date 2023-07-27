package pize.tests.voxelgame.client.block

import pize.math.vecmath.matrix.Matrix4f
import pize.math.vecmath.vector.Vec3i

enum class BlockRotation(x: Int, y: Int, z: Int, angle: Int) {
    X90(1, 0, 0, 90),
    X180(1, 0, 0, 180),
    X270(1, 0, 0, 270),
    Y90(0, 1, 0, 90),
    Y180(0, 1, 0, 180),
    Y270(0, 1, 0, 270),
    Z90(0, 0, 1, 90),
    Z180(0, 0, 1, 180),
    Z270(0, 0, 1, 270);

    val axis: Vec3i
    val angle: Int
    val matrix: Matrix4f

    init {
        axis = Vec3i(x, y, z)
        this.angle = angle
        matrix = Matrix4f()
        if (x == 1) matrix.toRotatedX(angle.toDouble()) else if (y == 1) matrix.toRotatedY(angle.toDouble()) else if (z == 1) matrix.toRotatedZ(
            angle.toDouble()
        )
    }
}
