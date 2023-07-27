package pize.graphics.util.batch.scissor

import pize.graphics.gl.Gl
import pize.graphics.gl.Target
import pize.graphics.util.batch.TextureBatch
import pize.math.Maths
import kotlin.math.max
import kotlin.math.min

class Scissor(private val batch: TextureBatch) {
    private val scissorList: MutableMap<Int, ScissorNode>

    init {
        scissorList = HashMap()
    }

    fun begin(index: Int, x: Double, y: Double, width: Double, height: Double) {
        this.begin(index, Maths.round(x), Maths.round(y), Maths.round(width), Maths.round(height))
    }

    fun begin(index: Int, x: Double, y: Double, width: Double, height: Double, scissorOfIndex: Int) {
        this.begin(index, Maths.round(x), Maths.round(y), Maths.round(width), Maths.round(height), scissorOfIndex)
    }

    fun begin(scissor: ScissorNode) {
        if (scissor.index < 0) return
        if (scissor.scissorOfIndex != -1 && !scissorList.isEmpty()) {
            val scissorOf = scissorList[scissor.scissorOfIndex]
            val scissorOfX2 = scissorOf.getX2()
            val scissorOfY2 = scissorOf.getY2()
            val oldX2 = scissor.x2
            val oldY2 = scissor.y2
            scissor.rectangle.x = max(min(scissor.x.toDouble(), scissorOfX2.toDouble()), scissorOf.getX().toDouble())
            scissor.rectangle.y = max(min(scissor.y.toDouble(), scissorOfY2.toDouble()), scissorOf.getY().toDouble())
            scissor.rectangle.width = max(
                0.0,
                min(
                    min(scissor.width.toDouble(), (oldX2 - scissorOf.getX()).toDouble()),
                    (min(scissorOfX2.toDouble(), scissor.x2.toDouble()) - scissor.x).toDouble()
                )
            )
            scissor.rectangle.height = max(
                0.0,
                min(
                    min(scissor.height.toDouble(), (oldY2 - scissorOf.getY()).toDouble()),
                    (min(scissorOfY2.toDouble(), scissor.y2.toDouble()) - scissor.y).toDouble()
                )
            )
        }
        scissorList[scissor.index] = scissor
        batch.end()
        if (!Gl.isEnabled(Target.SCISSOR_TEST)) Gl.enable(Target.SCISSOR_TEST)
        scissor.activate()
    }

    @JvmOverloads
    fun begin(index: Int, x: Int, y: Int, width: Int, height: Int, scissorOfIndex: Int = -1) {
        var x = x
        var y = y
        var width = width
        var height = height
        if (index < 0) return
        if (scissorOfIndex != -1 && !scissorList.isEmpty()) {
            val scissorOf = scissorList[scissorOfIndex]
            val scissorOfX2 = scissorOf.getX2()
            val scissorOfY2 = scissorOf.getY2()
            val oldX2 = x + width
            val oldY2 = y + height
            x = max(min(x.toDouble(), scissorOfX2.toDouble()), scissorOf.getX().toDouble()).toInt()
            y = max(min(y.toDouble(), scissorOfY2.toDouble()), scissorOf.getY().toDouble()).toInt()
            val x2 = x + width
            val y2 = y + height
            width = max(
                0.0,
                min(
                    min(width.toDouble(), (oldX2 - scissorOf.getX()).toDouble()),
                    (min(scissorOfX2.toDouble(), x2.toDouble()) - x).toDouble()
                )
            ).toInt()
            height = max(
                0.0,
                min(
                    min(height.toDouble(), (oldY2 - scissorOf.getY()).toDouble()),
                    (min(scissorOfY2.toDouble(), y2.toDouble()) - y).toDouble()
                )
            ).toInt()
        }
        val scissor = ScissorNode(index, x, y, width, height, scissorOfIndex)
        scissorList[index] = scissor
        batch.end()
        if (!Gl.isEnabled(Target.SCISSOR_TEST)) Gl.enable(Target.SCISSOR_TEST)
        scissor.activate()
    }

    fun end(index: Int) {
        val removedScissor = scissorList.remove(index)
        val removedIndexOf = removedScissor.getScissorOfIndex()
        batch.end()
        if (removedIndexOf != -1 && scissorList.size != 0) {
            val scissor = scissorList[removedIndexOf]
            scissor!!.activate()
        } else if (Gl.isEnabled(Target.SCISSOR_TEST)) Gl.disable(Target.SCISSOR_TEST)
    }
}
