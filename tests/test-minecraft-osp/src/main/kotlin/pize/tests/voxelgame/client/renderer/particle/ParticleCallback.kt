package pize.tests.voxelgame.client.renderer.particle

fun interface ParticleCallback {
    operator fun invoke(instance: ParticleInstance?)
}
