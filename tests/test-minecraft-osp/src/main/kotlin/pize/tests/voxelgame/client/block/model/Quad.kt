package pize.tests.voxelgame.client.block.model

import pize.math.vecmath.vector.Vec3f

open class Quad(protected val p1: Vec3f, protected val p2: Vec3f, protected val p3: Vec3f, protected val p4: Vec3f) {
    constructor(
        x1: Float,
        y1: Float,
        z1: Float,
        x2: Float,
        y2: Float,
        z2: Float,
        x3: Float,
        y3: Float,
        z3: Float,
        x4: Float,
        y4: Float,
        z4: Float
    ) : this(
        Vec3f(x1, y1, z1),
        Vec3f(x2, y2, z2),
        Vec3f(x3, y3, z3),
        Vec3f(x4, y4, z4)
    )

    constructor(quad: Quad) : this(
        quad.p1.copy(),
        quad.p2.copy(),
        quad.p3.copy(),
        quad.p4.copy()
    )

    open fun copy(): Quad {
        return Quad(this)
    }

    private fun nx() {
        // x  , y+1, z  , x  , y  , z  , x  , y  , z+1, x  , y+1, z+1

        // x  , y+1, z+1, region.u2(), region.v1();
        // x  , y  , z+1, region.u2(), region.v2();
        // x  , y  , z  , region.u1(), region.v2();
        // x  , y+1, z  , region.u1(), region.v1();
    }

    private fun px() {
        // x+1, y+1, z+1, x+1, y  , z+1, x+1, y  , z  , x+1, y+1, z

        // x+1, y+1, z  , region.u2(), region.v1();
        // x+1, y  , z  , region.u2(), region.v2();
        // x+1, y  , z+1, region.u1(), region.v2();
        // x+1, y+1, z+1, region.u1(), region.v1();
    }

    private fun ny() {
        // x  , y  , z+1, x  , y  , z  , x+1, y  , z  , x+1, y  , z+1

        // x+1, y  , z  , region.u2(), region.v2();
        // x  , y  , z  , region.u1(), region.v2();
        // x  , y  , z+1, region.u1(), region.v1();
        // x+1, y  , z+1, region.u2(), region.v1();
    }

    private fun py() {
        // x  , y+1, z  , x  , y+1, z+1, x+1, y+1, z+1, x+1, y+1, z

        // x  , y+1, z  , region.u1(), region.v1();
        // x+1, y+1, z  , region.u2(), region.v1();
        // x+1, y+1, z+1, region.u2(), region.v2();
        // x  , y+1, z+1, region.u1(), region.v2();
    }

    private fun nz() {
        // x+1, y+1, z  , x+1, y  , z  , x  , y  , z  , x  , y+1, z

        // x  , y  , z  , region.u2(), region.v2();
        // x+1, y  , z  , region.u1(), region.v2();
        // x+1, y+1, z  , region.u1(), region.v1();
        // x  , y+1, z  , region.u2(), region.v1();
    }

    private fun pz() {
        // x  , y+1, z+1, x  , y  , z+1, x+1, y  , z+1, x+1, y+1, z+1

        // x+1, y  , z+1, region.u2(), region.v2();
        // x  , y  , z+1, region.u1(), region.v2();
        // x  , y+1, z+1, region.u1(), region.v1();
        // x+1, y+1, z+1, region.u2(), region.v1();
    }

    companion object {
        val nxQuad = Quad(0f, 1f, 1f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f, 0f)
            get() = field.copy()
        val pxQuad = Quad(1f, 1f, 0f, 1f, 0f, 0f, 1f, 0f, 1f, 1f, 1f, 1f)
            get() = field.copy()
        val nyQuad = Quad(1f, 0f, 1f, 1f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 1f)
            get() = field.copy()
        val pyQuad = Quad(1f, 1f, 0f, 1f, 1f, 1f, 0f, 1f, 1f, 0f, 1f, 0f)
            get() = field.copy()
        val nzQuad = Quad(0f, 1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 1f, 1f, 0f)
            get() = field.copy()
        val pzQuad = Quad(1f, 1f, 1f, 1f, 0f, 1f, 0f, 0f, 1f, 0f, 1f, 1f)
            get() = field.copy()
    }
}
