package pize.tests.voxelgame.client.renderer.particle

import pize.Pize.dt
import pize.graphics.camera.Camera3D.y
import pize.graphics.texture.Region.set
import pize.graphics.texture.Texture
import pize.graphics.util.color.Color.mul
import pize.graphics.util.color.Color.set
import pize.math.Maths.random
import pize.math.util.EulerAngles.set
import pize.math.vecmath.matrix.Matrix4f.mul
import pize.math.vecmath.matrix.Matrix4f.set
import pize.math.vecmath.vector.Vec2f.set
import pize.math.vecmath.vector.Vec3f.mul
import pize.math.vecmath.vector.Vec3f.set
import pize.math.vecmath.vector.Vec3i.mul
import pize.math.vecmath.vector.Vec3i.set

enum class Particles(val particle: Particle?) {
    BLOCK_BREAK(Particle()
        .init { instance: ParticleInstance? ->
            instance!!.size = random(0.05f, 0.25f)
            instance.velocity.set(random(-0.05f, 0.05f), 0f, random(-0.05f, 0.05f))
        }
        .texture(Texture("texture/block/planks.png"))
        .alphaFunc { time: Float? -> 1 - time!! }
        .animate { instance: ParticleInstance? ->
            instance!!.velocity.y -= (dt * 0.5).toFloat()
            instance.velocity.mul(0.95)
            instance.position.add(instance.velocity)
        }
    )

}
