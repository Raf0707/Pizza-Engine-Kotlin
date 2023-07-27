package pize.tests.voxelgame.client.entity

import pize.Pize.execSync
import pize.Pize.updateDt
import pize.math.util.EulerAngles
import pize.math.vecmath.vector.Vec3f
import pize.tests.voxelgame.client.entity.model.PlayerModel
import pize.tests.voxelgame.client.level.ClientLevel
import pize.tests.voxelgame.main.entity.Player
import pize.tests.voxelgame.main.level.Level

open class AbstractClientPlayer(level: Level?, name: String?) : Player(level, name) {
    var model: PlayerModel? = null
        private set
    private var lastTime: Long
    private val lastPosition: Vec3f
    val lerpPosition: Vec3f
    private val lastRotation: EulerAngles
    val lerpRotation: EulerAngles

    init {

        // Interpolation
        lastTime = System.currentTimeMillis()
        lastPosition = Vec3f()
        lerpPosition = Vec3f()
        lastRotation = EulerAngles()
        lerpRotation = EulerAngles()
    }

    override var level: Level?
        get() = super.getLevel() as ClientLevel
        set(level) {
            super.level = level
        }

    override fun tick() {
        // Interpolation
        lastTime = System.currentTimeMillis()
        lastPosition.set(position)
        lastRotation.set(rotation)

        // Player model
        if (model == null) {
            execSync {
                println(1)
                model = PlayerModel(this)
            }
        }

        // Player tick
        super.tick()
    }

    fun updateInterpolation() {
        val lastTickTime = (System.currentTimeMillis() - lastTime) / 1000f / updateDt
        lerpPosition.lerp(lastPosition, position, lastTickTime)
        lerpRotation.lerp(lastRotation, rotation, lastTickTime)
    }

    val isPositionChanged: Boolean
        get() = !lastPosition.equals(position)
    val isRotationChanged: Boolean
        get() = lastRotation != rotation
}
