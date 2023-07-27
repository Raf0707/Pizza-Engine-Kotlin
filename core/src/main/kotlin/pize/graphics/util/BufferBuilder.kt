package pize.graphics.util

import pize.graphics.gl.BufferUsage
import pize.graphics.texture.Region
import pize.graphics.util.color.IColor
import pize.graphics.vertex.VertexBuffer

class BufferBuilder {
    private val vertices: MutableList<Float>

    init {
        vertices = ArrayList()
    }

    fun array(vararg array: Float): BufferBuilder {
        for (value in array) vertices.add(value)
        return this
    }

    fun array(vararg array: Double): BufferBuilder {
        for (value in array) vertices.add(value.toFloat())
        return this
    }

    fun vertex(x: Double, y: Double): BufferBuilder {
        vertices.add(x.toFloat())
        vertices.add(y.toFloat())
        return this
    }

    fun vertex(x: Double, y: Double, z: Double): BufferBuilder {
        vertices.add(x.toFloat())
        vertices.add(y.toFloat())
        vertices.add(z.toFloat())
        return this
    }

    @JvmOverloads
    fun color(r: Double = 1.0, g: Double = 1.0, b: Double = 1.0): BufferBuilder {
        vertices.add(r.toFloat())
        vertices.add(g.toFloat())
        vertices.add(b.toFloat())
        vertices.add(1f)
        return this
    }

    fun color(r: Double, g: Double, b: Double, a: Double): BufferBuilder {
        vertices.add(r.toFloat())
        vertices.add(g.toFloat())
        vertices.add(b.toFloat())
        vertices.add(a.toFloat())
        return this
    }

    fun color(color: IColor): BufferBuilder {
        vertices.add(color.r())
        vertices.add(color.g())
        vertices.add(color.b())
        vertices.add(color.a())
        return this
    }

    fun uv(u: Float, v: Float): BufferBuilder {
        vertices.add(u)
        vertices.add(v)
        return this
    }

    fun uv(u: Double, v: Double): BufferBuilder {
        vertices.add(u.toFloat())
        vertices.add(v.toFloat())
        return this
    }

    fun quad(
        x1: Double,
        y1: Double,
        z1: Double,
        x2: Double,
        y2: Double,
        z2: Double,
        x3: Double,
        y3: Double,
        z3: Double,
        x4: Double,
        y4: Double,
        z4: Double,
        u1: Double,
        v1: Double,
        u2: Double,
        v2: Double,
        color: IColor
    ): BufferBuilder {
        vertex(x1, y1, z1).color(color).uv(u1, v1) // 1
        vertex(x2, y2, z2).color(color).uv(u1, v2) // 2
        vertex(x3, y3, z3).color(color).uv(u2, v2) // 3
        vertex(x3, y3, z3).color(color).uv(u2, v2) // 3
        vertex(x4, y4, z4).color(color).uv(u2, v1) // 4
        vertex(x1, y1, z1).color(color).uv(u1, v1) // 1
        return this
    }

    fun quad(
        x1: Double,
        y1: Double,
        z1: Double,
        x2: Double,
        y2: Double,
        z2: Double,
        x3: Double,
        y3: Double,
        z3: Double,
        x4: Double,
        y4: Double,
        z4: Double,
        region: Region,
        color: IColor
    ): BufferBuilder {
        return quad(
            x1,
            y1,
            z1,
            x2,
            y2,
            z2,
            x3,
            y3,
            z3,
            x4,
            y4,
            z4,
            region.u1().toDouble(),
            region.v1().toDouble(),
            region.u2().toDouble(),
            region.v2().toDouble(),
            color
        )
    }

    fun end(buffer: VertexBuffer) {
        val array = FloatArray(vertices.size)
        for (i in array.indices) array[i] = vertices[i]
        vertices.clear()
        buffer.setData(array, BufferUsage.DYNAMIC_DRAW)
    }

    fun end(list: MutableList<Float>) {
        list.addAll(vertices)
        vertices.clear()
    }
}
