package pize.tests.voxelgame.client.renderer.particle

import pize.graphics.texture.Texture
import pize.graphics.util.color.Color
import pize.math.vecmath.vector.Vec3f
import java.util.function.Function

class Particle {
    var texture: Texture? = null
    protected var initialSetup: ParticleCallback? = null
    var alphaFunc: Function<Float?, Float>
    var onAnimate: ParticleCallback
    var color: Color

    init {
        alphaFunc = DEFAULT_ALPHA_FUNC
        onAnimate = ParticleCallback { instance: ParticleInstance? -> }
        color = Color()
    }

    fun texture(texture: Texture?): Particle {
        this.texture = texture
        return this
    }

    fun init(initialSetup: ParticleCallback?): Particle {
        this.initialSetup = initialSetup
        return this
    }

    fun alphaFunc(alphaFunc: Function<Float?, Float>): Particle {
        this.alphaFunc = alphaFunc
        return this
    }

    fun animate(onAnimate: ParticleCallback): Particle {
        this.onAnimate = onAnimate
        return this
    }

    fun createInstance(position: Vec3f): ParticleInstance {
        val instance = ParticleInstance(this, position)
        initialSetup!!.invoke(instance)
        return instance
    }

    companion object {
        val DEFAULT_ALPHA_FUNC = Function { time: Float? -> 1f }
    }
}
