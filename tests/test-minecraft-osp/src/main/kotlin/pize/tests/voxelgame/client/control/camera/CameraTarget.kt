package pize.tests.voxelgame.client.control.camera

import pize.math.util.EulerAngles
import pize.math.vecmath.vector.Vec3f

interface CameraTarget {
    val position: Vec3f
    val rotation: EulerAngles
}
