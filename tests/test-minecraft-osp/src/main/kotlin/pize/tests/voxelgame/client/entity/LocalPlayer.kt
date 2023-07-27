package pize.tests.voxelgame.client.entity

import pize.graphics.camera.Camera3D.x
import pize.graphics.camera.Camera3D.y
import pize.graphics.camera.Camera3D.z
import pize.graphics.util.color.Color.mul
import pize.math.Mathc.cos
import pize.math.Mathc.sin
import pize.math.Maths
import pize.math.vecmath.matrix.Matrix4f.mul
import pize.math.vecmath.vector.Vec3f
import pize.math.vecmath.vector.Vec3f.add
import pize.math.vecmath.vector.Vec3f.mul
import pize.math.vecmath.vector.Vec3i.add
import pize.math.vecmath.vector.Vec3i.mul
import pize.tests.voxelgame.client.block.Blocks
import pize.tests.voxelgame.main.level.Level

class LocalPlayer(levelOF: Level?, name: String?) : AbstractClientPlayer(levelOF, name) {
    private val moveControl: Vec3f
    private var jumpDownY = 0f
    private var lastVelocityY = 0f
    var fallHeight = 0f
        private set
    var holdBlock = Blocks.GRASS

    init {
        moveControl = Vec3f()
    }

    override fun tick() {
        super.tick()
        /** -------- Vertical Move --------  */

        // Jumping
        if (isJumping) {
            if (isOnGround) {
                // Jump
                velocity.y = 0.42f

                // Jump-boost
                if (isSprinting) {
                    val yaw = rotation.yaw * Maths.ToRad
                    val jumpBoost = 0.2f
                    velocity.x += jumpBoost * cos(yaw.toDouble())
                    velocity.z += jumpBoost * sin(yaw.toDouble())
                }
            }
        }

        // Interrupt Flying
        if (isOnGround && isFlying) isFlying = false
        if (isFlying) {
            if (isSneaking) velocity.y -= 0.05f
            if (isJumping) velocity.y += 0.05f
            if (!isFlyEnabled) isFlying = false
        }

        // In Water
        val position = position
        if (level.getBlock(position.xf(), position.yf(), position.zf()) == Blocks.WATER.id.toShort()) {
            //getVelocity().x += Maths.random(0, 2) * Maths.cosDeg(getRotation().yaw);
            //getVelocity().y += 0.4;
            //getVelocity().z += Maths.random(0, 2) * Maths.sinDeg(getRotation().yaw);
            //Pize.execSync(() -> getLevel().getSession().getAudioPlayer().play(SoundGroup.HIT.random(), 0.3F, 1, 0, 0, 0) );
            //getLevel().getSession().getGame().getCamera().getRotation().roll = 50F;
        }
        level.session.game.camera.rotation.roll /= 1.4f

        // Gravity
        if (!isOnGround && !isFlying) velocity.y -= 0.08.toFloat()

        // Reduce Vertical Motion
        velocity.y *= 0.98.toFloat()
        /** -------- Horizontal Move --------  */

        // Movement multiplier
        var movementMul = 0.98f // Default
        if (isSneaking && !isFlying) movementMul *= 10f // Sneaking
        if (isSprinting) movementMul *= 10f // Sprinting
        if (isFlying) movementMul *= 100f // Flying


        // Slipperiness multiplier
        var slipperinessMul = 1f // Air
        if (isOnGround) slipperinessMul *= 0.9.toFloat() // Ground

        // Reduce Last Motion
        val reduceHorizontal = slipperinessMul * 0.91f
        velocity.mul(reduceHorizontal, 1f, reduceHorizontal)

        // Move
        val moveControlLen = moveControl.len()
        if (moveControlLen > 0) {
            val acceleration = Vec3f(moveControl.x, 0f, moveControl.z)
            if (isOnGround) {
                val slipperiness = 0.6f / slipperinessMul
                acceleration.mul(0.1 * movementMul * slipperiness * slipperiness * slipperiness)
            } else acceleration.mul(0.02 * movementMul)
            velocity.add(acceleration)
        }
        /** -------- Other --------  */

        // Fall height
        if (velocity.y < 0 && lastVelocityY >= 0) jumpDownY = position.y
        if (isOnGround && jumpDownY != 0f) {
            fallHeight = jumpDownY - position.y
            jumpDownY = 0f
        }
        lastVelocityY = velocity.y

        // Move entity
        val collidedMotion = moveEntity(velocity)
        velocity.collidedAxesToZero(collidedMotion)

        // Disable sprinting
        if (collidedMotion!!.x == 0f || collidedMotion.z == 0f) isSprinting = false
    }

    fun moveControl(motion: Vec3f?) {
        moveControl.set(motion!!)
    }
}
