package pize.tests.voxelgame.client.block.model

import pize.graphics.util.color.IColor
import pize.math.vecmath.vector.Vec2f
import pize.math.vecmath.vector.Vec3f

class Vertex(private val position: Vec3f, private val uv: Vec2f, private val color: IColor) {
    constructor(position: Vec3f, u: Float, v: Float, color: IColor) : this(position, Vec2f(u, v), color)

    fun putFloats(
        floatList: MutableList<Float?>,
        x: Float,
        y: Float,
        z: Float,
        r: Float,
        g: Float,
        b: Float,
        a: Float
    ) {
        floatList.add(position.x + x)
        floatList.add(position.y + y)
        floatList.add(position.z + z)
        floatList.add(color.r() * r)
        floatList.add(color.g() * g)
        floatList.add(color.b() * b)
        floatList.add(color.a() * a)
        floatList.add(uv.x)
        floatList.add(uv.y)
    }

    fun putIntsPacked(
        intList: MutableList<Int?>,
        x: Int,
        y: Int,
        z: Int,
        mulU: Int,
        mulV: Int,
        r: Float,
        g: Float,
        b: Float,
        a: Float
    ) {
        // Packed position
        val positionPacked = (position.x + x).toInt() or  // 5 bit
                ((position.y + y).toInt() shl 5) or  // 9 bit
                ((position.z + z).toInt() shl 14) or  // 5 bit
                ((uv.x * mulU).toInt() shl 19) or  // 4 bit
                ((uv.y * mulV).toInt() shl 23) // 4 bit
        // 4 bit remaining
        intList.add(positionPacked) // x, y, z, u, v

        // Packed color
        val colorPacked = (color.r() * r * 255).toInt() or  // 8 bit
                ((color.g() * g * 255).toInt() shl 8) or  // 8 bit
                ((color.b() * b * 255).toInt() shl 16) or  // 8 bit
                ((color.a() * a * 255).toInt() shl 24) // 8 bit
        intList.add(colorPacked) // r, g, b, a
    }
}
