package pize.tests.voxelgame.main.entity

import pize.tests.voxelgame.main.level.Level

open class Player(level: Level?, val name: String?) : Entity(EntityType.Companion.PLAYER, level) {
    var isSprinting = false
        set(sprinting) {
            field = if (sprinting && isCollidedTo(rotation.direction.mul(1, 0, 1))) false else sprinting
        }
    private var sneaking = false
    open var isFlyEnabled = false
    var isFlying = false
    var isJumping = false

    fun isSneaking(): Boolean {
        return sneaking
    }

    fun setSneaking(sneaking: Boolean) {
        this.sneaking = sneaking
        boundingBox!!.max.y = if (sneaking) 1.5f else 1.8f
    }

    override val eyeHeight: Float
        get() = if (sneaking) 1.27f else 1.62f
}
