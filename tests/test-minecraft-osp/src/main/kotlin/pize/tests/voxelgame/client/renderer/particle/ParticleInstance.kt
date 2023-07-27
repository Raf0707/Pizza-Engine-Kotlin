package pize.tests.voxelgame.client.renderer.particle

import pize.graphics.texture.Region
import pize.graphics.util.color.Color
import pize.math.Maths.clamp
import pize.math.vecmath.vector.Vec3f
import pize.physic.Velocity3f

class ParticleInstance(val particle: Particle, position: Vec3f) {
    private val creationTime: Long
    var elapsedTimeSeconds = 0f
        private set
    var alpha = 0f
        private set
    var region: Region
    var lifeTimeSeconds = 0f
    var position: Vec3f
    var velocity: Velocity3f
    var rotation = 0f
    var size = 0f
    var color: Color

    init {
        region = Region()
        this.position = position.copy()
        creationTime = System.currentTimeMillis()
        color = particle.color.copy()
        velocity = Velocity3f()
    }

    fun update() {
        // Alpha
        elapsedTimeSeconds = (System.currentTimeMillis() - creationTime) / 1000f
        alpha = particle.alphaFunc.apply(elapsedTimeSeconds / lifeTimeSeconds)
        alpha = clamp(alpha, 0f, 1f)
        // Animate
        particle.onAnimate.invoke(this)
    }
}
