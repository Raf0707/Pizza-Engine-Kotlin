package pize.physic

import pize.math.vecmath.vector.Vec3f
import pize.physic.Intersector

class BoundingBox3 {
    val min: Vec3f
    val max: Vec3f

    constructor(minX: Float, minY: Double, minZ: Double, maxX: Double, maxY: Double, maxZ: Double) {
        min = Vec3f(minX, minY, minZ)
        max = Vec3f(maxX, maxY, maxZ)
    }

    constructor(min: Vec3f, max: Vec3f) {
        this.min = min
        this.max = max
    }

    constructor(box: BoundingBox3) {
        min = box.min.copy()
        max = box.max.copy()
    }

    fun resize(minX: Double, minY: Double, minZ: Double, maxX: Double, maxY: Double, maxZ: Double) {
        min[minX, minY] = minZ
        max[maxX, maxY] = maxZ
    }

    fun resize(min: Vec3f?, max: Vec3f?) {
        min?.let { this.min.set(it) }
        max?.let { this.max.set(it) }
    }

    fun resize(box: BoundingBox3) {
        resize(box.min, box.max)
    }

    fun expand(
        negativeX: Double,
        negativeY: Double,
        negativeZ: Double,
        positiveX: Double,
        positiveY: Double,
        positiveZ: Double
    ) {
        resize(
            min.x - negativeX,
            min.y - negativeY,
            min.z - negativeZ,
            max.x + positiveX,
            max.y + positiveY,
            max.z + positiveZ
        )
    }

    fun expand(negative: Vec3f, positive: Vec3f) {
        expand(
            negative.x.toDouble(),
            negative.y.toDouble(),
            negative.z.toDouble(),
            positive.x.toDouble(),
            positive.y.toDouble(),
            positive.z.toDouble()
        )
    }

    fun expand(expandX: Double, expandY: Double, expandZ: Double) {
        expand(expandX, expandY, expandZ, expandX, expandY, expandZ)
    }

    fun expand(expand: Double) {
        expand(expand, expand, expand)
    }

    fun shift(shiftX: Double, shiftY: Double, shiftZ: Double) {
        resize(min.x + shiftX, min.y + shiftY, min.z + shiftZ, max.x + shiftX, max.y + shiftY, max.z + shiftZ)
    }

    fun shift(shift: Vec3f) {
        shift(shift.x.toDouble(), shift.y.toDouble(), shift.z.toDouble())
    }

    val sizeX: Float
        get() = max.x - min.x
    val sizeY: Float
        get() = max.y - min.y
    val sizeZ: Float
        get() = max.z - min.z
    val volume: Float
        get() = sizeY * sizeX * sizeZ
    val centerX: Float
        get() = min.x + sizeX * 0.5f
    val centerY: Float
        get() = min.y + sizeY * 0.5f
    val centerZ: Float
        get() = min.z + sizeZ * 0.5f
    val center: Vec3f
        get() = Vec3f(centerX, centerY, centerZ)

    fun copy(): BoundingBox3 {
        return BoundingBox3(this)
    }
}