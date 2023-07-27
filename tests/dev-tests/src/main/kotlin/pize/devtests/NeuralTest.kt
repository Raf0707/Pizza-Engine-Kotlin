package pize.devtests

import pize.math.Maths.mul
import pize.math.Maths.sigmoid

class NeuralTest internal constructor() {
    internal open inner class LastLayer(size: Int) {
        var neurons: FloatArray

        init {
            neurons = FloatArray(size)
        }

        fun print() {
            if (neurons.size == 1) {
                println(neurons[0])
                return
            }
            println(neurons.contentToString())
        }

        fun set(vararg neurons: Float) {
            System.arraycopy(neurons, 0, this.neurons, 0, neurons.size)
        }

        fun sigmoid() {
            val shift = 1
            for (i in neurons.indices) neurons[i] = sigmoid(neurons[i] + shift)
        }
    }

    internal inner class Layer(size: Int) : LastLayer(size) {
        var weights: FloatArray

        init {
            weights = FloatArray(size)
        }

        fun mulTo(layer: LastLayer?) {
            mul(neurons, weights, layer!!.neurons)
            layer.sigmoid()
        }

        fun setWeights(vararg weights: Float) {
            System.arraycopy(weights, 0, this.weights, 0, weights.size)
        }

        fun backprop(prevLayer: Layer?, outData: FloatArray) {
            for (i in outData.indices) {
                val error = neurons[i] - outData[i]
            }
        }
    }

    internal inner class Network(hiddenLayersNum: Int) {
        var `in`: Layer? = null
        var hidden: Array<Layer?>
        var out: LastLayer? = null

        init {
            hidden = arrayOfNulls(hiddenLayersNum)
        }

        fun layer(index: Int): Layer? {
            return hidden[index]
        }

        fun def(index: Int, size: Int) {
            hidden[index + 1] = Layer(size)
        }

        fun defIn(size: Int) {
            `in` = Layer(size)
        }

        fun defOut(size: Int) {
            out = LastLayer(size)
        }

        fun process() {
            if (hidden.size == 0) {
                `in`!!.mulTo(out)
                return
            }
            val last = hidden.size - 1
            `in`!!.mulTo(hidden[0])
            for (i in 0 until last) hidden[i]!!.mulTo(hidden[i + 1])
            hidden[last]!!.mulTo(out)
        }
    }

    init {
        val network = Network(0)
        network.defIn(3)
        network.defOut(1)
        network.`in`!!.set(1f, 0f, -0.5f)
        network.`in`!!.setWeights(10f, 0f, -5f)
        network.process()
        network.out!!.print()
    }
}
