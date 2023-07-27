package pize.tests.voxelgame.client.block.model

import pize.graphics.texture.Region
import pize.graphics.util.color.IColor
import pize.graphics.util.color.ImmutableColor
import pize.math.vecmath.vector.Vec2f
import pize.math.vecmath.vector.Vec3f
import pize.tests.voxelgame.client.block.BlockRotation

class Face : Quad {
    protected val t1: Vec2f
    protected val t2: Vec2f
    protected val t3: Vec2f
    protected val t4: Vec2f
    protected var color: IColor

    constructor(face: Face) : super(face) {
        t1 = face.t1.copy()
        t2 = face.t2.copy()
        t3 = face.t3.copy()
        t4 = face.t4.copy()
        color = ImmutableColor(face.color)
    }

    constructor(quad: Quad, t1: Vec2f, t2: Vec2f, t3: Vec2f, t4: Vec2f, color: IColor) : super(quad) {
        this.t1 = t1
        this.t2 = t2
        this.t3 = t3
        this.t4 = t4
        this.color = color
    }

    constructor(
        p1: Vec3f,
        p2: Vec3f,
        p3: Vec3f,
        p4: Vec3f,
        t1: Vec2f,
        t2: Vec2f,
        t3: Vec2f,
        t4: Vec2f,
        color: IColor
    ) : this(
        Quad(p1, p2, p3, p4),
        t1,
        t2,
        t3,
        t4,
        color
    )

    constructor(quad: Quad, region: Region?, color: IColor) : this(
        quad,
        Vec2f(region!!.u1(), region.v1()),
        Vec2f(region.u1(), region.v2()),
        Vec2f(region.u2(), region.v2()),
        Vec2f(region.u2(), region.v1()),
        color
    )

    constructor(p1: Vec3f, p2: Vec3f, p3: Vec3f, p4: Vec3f, region: Region?, color: IColor) : this(
        Quad(p1, p2, p3, p4),
        region,
        color
    )

    fun putFloats(
        floatList: MutableList<Float?>?,
        x: Float,
        y: Float,
        z: Float,
        l1: Float,
        l2: Float,
        l3: Float,
        l4: Float
    ) {
        val p1x = p1.x + x
        val p1y = p1.y + y
        val p1z = p1.z + z // p1
        val p3x = p3.x + x
        val p3y = p3.y + y
        val p3z = p3.z + z // p3

        // Triangle 1
        putVertex(floatList, p1x, p1y, p1z, t1.x, t1.y, l1) // 1       1 ------ 4
        putVertex(floatList, p2.x + x, p2.y + y, p2.z + z, t2.x, t2.y, l2) // 2       |  ╲╲    |
        putVertex(floatList, p3x, p3y, p3z, t3.x, t3.y, l3) // 3       |    ╲╲  |
        // Triangle 2                                                                     2 ------ 3
        putVertex(floatList, p3x, p3y, p3z, t3.x, t3.y, l3) // 3
        putVertex(floatList, p4.x + x, p4.y + y, p4.z + z, t4.x, t4.y, l4) // 4
        putVertex(floatList, p1x, p1y, p1z, t1.x, t1.y, l1) // 1
    }

    fun putFloatsFlipped(
        floatList: MutableList<Float?>?,
        x: Float,
        y: Float,
        z: Float,
        l1: Float,
        l2: Float,
        l3: Float,
        l4: Float
    ) {
        val p4x = p4.x + x
        val p4y = p4.y + y
        val p4z = p4.z + z // p4
        val p2x = p2.x + x
        val p2y = p2.y + y
        val p2z = p2.z + z // p2

        // Triangle 1
        putVertex(floatList, p4x, p4y, p4z, t4.x, t4.y, l4) // 4        1 ------ 4
        putVertex(floatList, p1.x + x, p1.y + y, p1.z + z, t1.x, t1.y, l1) // 1        |    ╱╱  |
        putVertex(floatList, p2x, p2y, p2z, t2.x, t2.y, l2) // 2        |  ╱╱    |
        // Triangle 2                                                                      2 ------ 3
        putVertex(floatList, p2x, p2y, p2z, t2.x, t2.y, l2) // 2
        putVertex(floatList, p3.x + x, p3.y + y, p3.z + z, t3.x, t3.y, l3) // 3
        putVertex(floatList, p4x, p4y, p4z, t4.x, t4.y, l4) // 4
    }

    fun putIntsPacked(intList: MutableList<Int?>?, x: Int, y: Int, z: Int, l1: Float, l2: Float, l3: Float, l4: Float) {
        val p1x = p1.x + x
        val p1y = p1.y + y
        val p1z = p1.z + z // p1
        val p3x = p3.x + x
        val p3y = p3.y + y
        val p3z = p3.z + z // p3
        putVertexPacked(intList, p1x, p1y, p1z, t1.x, t1.y, l1) // 1
        putVertexPacked(intList, p2.x + x, p2.y + y, p2.z + z, t2.x, t2.y, l2) // 2
        putVertexPacked(intList, p3x, p3y, p3z, t3.x, t3.y, l3) // 3
        putVertexPacked(intList, p3x, p3y, p3z, t3.x, t3.y, l3) // 3
        putVertexPacked(intList, p4.x + x, p4.y + y, p4.z + z, t4.x, t4.y, l4) // 4
        putVertexPacked(intList, p1x, p1y, p1z, t1.x, t1.y, l1) // 1
    }

    fun putIntsPackedFlipped(
        intList: MutableList<Int?>?,
        x: Int,
        y: Int,
        z: Int,
        l1: Float,
        l2: Float,
        l3: Float,
        l4: Float
    ) {
        val p4x = p4.x + x
        val p4y = p4.y + y
        val p4z = p4.z + z // p4
        val p2x = p2.x + x
        val p2y = p2.y + y
        val p2z = p2.z + z // p2
        putVertexPacked(intList, p4x, p4y, p4z, t4.x, t4.y, l4) // 4
        putVertexPacked(intList, p1.x + x, p1.y + y, p1.z + z, t1.x, t1.y, l1) // 1
        putVertexPacked(intList, p2x, p2y, p2z, t2.x, t2.y, l2) // 2
        putVertexPacked(intList, p2x, p2y, p2z, t2.x, t2.y, l2) // 2
        putVertexPacked(intList, p3.x + x, p3.y + y, p3.z + z, t3.x, t3.y, l3) // 3
        putVertexPacked(intList, p4x, p4y, p4z, t4.x, t4.y, l4) // 4
    }

    private fun putVertex(
        floatList: MutableList<Float?>?,
        x: Float,
        y: Float,
        z: Float,
        u: Float,
        v: Float,
        light: Float
    ) {
        floatList!!.add(x)
        floatList.add(y)
        floatList.add(z)
        floatList.add(color.r() * light)
        floatList.add(color.g() * light)
        floatList.add(color.b() * light)
        floatList.add(color.a())
        floatList.add(u)
        floatList.add(v)
    }

    private fun putVertexPacked(
        intList: MutableList<Int?>?,
        x: Float,
        y: Float,
        z: Float,
        u: Float,
        v: Float,
        light: Float
    ) {
        val mulU = 16
        val mulV = 16

        // Packed position
        val positionPacked = x.toInt() or  // 5 bit
                (y.toInt() shl 5) or  // 9 bit
                (z.toInt() shl 14) or  // 5 bit
                ((u * mulU).toInt() shl 19) or  // 4 bit
                ((v * mulV).toInt() shl 23) // 4 bit
        // 4 bit remaining
        intList!!.add(positionPacked) // x, y, z, u, v

        // Packed color
        val colorPacked = (color.r() * light * 255).toInt() or  // 8 bit
                ((color.g() * light * 255).toInt() shl 8) or  // 8 bit
                ((color.b() * light * 255).toInt() shl 16) or  // 8 bit
                ((color.a() * 255).toInt() shl 24) // 8 bit
        intList.add(colorPacked) // r, g, b, a
    }

    override fun copy(): Face {
        return Face(this)
    }

    fun rotated(rotation: BlockRotation): Face {
        val rp1 = p1.copy().sub(0.5).mul(rotation.matrix).add(0.5)
        val rp2 = p2.copy().sub(0.5).mul(rotation.matrix).add(0.5)
        val rp3 = p3.copy().sub(0.5).mul(rotation.matrix).add(0.5)
        val rp4 = p4.copy().sub(0.5).mul(rotation.matrix).add(0.5)
        return if (p1 === rp2) {
            Face(
                rp2,
                rp3,
                rp4,
                rp1,
                t1.copy(),
                t2.copy(),
                t3.copy(),
                t4.copy(),
                color
            )
        } else if (p1 === rp3) {
            Face(
                rp3,
                rp4,
                rp1,
                rp2,
                t1.copy(),
                t2.copy(),
                t3.copy(),
                t4.copy(),
                color
            )
        } else if (p1 === rp4) {
            Face(
                rp4,
                rp1,
                rp2,
                rp3,
                t1.copy(),
                t2.copy(),
                t3.copy(),
                t4.copy(),
                color
            )
        } else Face(
            rp1,
            rp2,
            rp3,
            rp4,
            t1.copy(),
            t2.copy(),
            t3.copy(),
            t4.copy(),
            color
        )
    }
}
