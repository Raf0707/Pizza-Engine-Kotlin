package pize.tests.voxelgame.main.entity

import pize.graphics.camera.Camera3D.x
import pize.graphics.camera.Camera3D.y
import pize.graphics.camera.Camera3D.z
import pize.math.Maths
import pize.math.Maths.ceil
import pize.math.Maths.floor
import pize.math.util.EulerAngles
import pize.math.vecmath.vector.Vec3f
import pize.physic.BoxBody
import pize.physic.Collider3f.getCollidedMotion
import pize.physic.Velocity3f
import pize.tests.voxelgame.main.Direction
import pize.tests.voxelgame.main.Tickable
import pize.tests.voxelgame.main.block.BlockData
import pize.tests.voxelgame.main.chunk.ChunkUtils
import pize.tests.voxelgame.main.level.Level
import java.util.*
import kotlin.math.max
import kotlin.math.min

abstract class Entity(val entityType: EntityType<*>, var level: Level?) : BoxBody(
    entityType.boundingBox
), Tickable {
    val rotation: EulerAngles
    val velocity: Velocity3f
    var uUID: UUID?
    private var blockBoxes: Array<BoxBody>?
    var isOnGround = false
        private set

    init {
        rotation = EulerAngles()
        velocity = Velocity3f()
        uUID = UUID.randomUUID()
    }

    open val eyeHeight: Float
        get() = boundingBox!!.sizeY * 0.85f

    override fun tick() {
        // Check is chunk loaded
        val pos = position
        if (level!!.getBlockChunk(pos.xf(), pos.zf()) == null) return

        // Update blocks around player
        blockBoxes = getBlockBoxes()
        // Update is player on ground
        isOnGround = isCollidedTo(Direction.NEGATIVE_Y)
    }

    fun isCollidedTo(face: Direction): Boolean {
        val dir = Vec3f(face.normal).mul(Maths.Epsilon)
        return getCollidedMotion(this, dir, *blockBoxes!!).len2() < dir.len2()
    }

    fun isCollidedTo(direction: Vec3f): Boolean {
        val dir = direction.copy().nor().mul(Maths.Epsilon)
        return getCollidedMotion(this, dir, *blockBoxes!!).len2() < dir.len2()
    }

    /** Get Bounding Boxes of blocks around Entity  */
    private fun getBlockBoxes(): Array<BoxBody> {
        val blockBoxes = ArrayList<BoxBody>()
        val velocity = velocity
        val min = min
        val max = max
        val beginX = floor(min.x - 0.5 + min(0.0, velocity.x.toDouble()))
        val beginY = max(
            0.0, min(
                ChunkUtils.HEIGHT_IDX.toDouble(),
                floor(min.y - 0.5 + min(0.0, velocity.y.toDouble()))
                    .toDouble()
            )
        ).toInt()
        val beginZ = floor(min.z - 0.5 + min(0.0, velocity.z.toDouble()))
        val endX = ceil(max.x + 0.5 + max(0.0, velocity.x.toDouble()))
        val endY = max(
            0.0, min(
                ChunkUtils.HEIGHT.toDouble(),
                ceil(max.y + 0.5 + max(0.0, velocity.y.toDouble()))
                    .toDouble()
            )
        ).toInt()
        val endZ = ceil(max.z + 0.5 + max(0.0, velocity.z.toDouble()))
        for (x in beginX until endX) for (y in beginY until endY) for (z in beginZ until endZ) {
            val blockData = level!!.getBlock(x, y, z)
            val blockState = BlockData.getState(blockData)
            val block = BlockData.getProps(blockData)
            if (block.states.isEmpty()) continue
            val shape = block!!.getState(blockState.toInt()).collide ?: continue
            for (boundingBox in shape.boxes) {
                val box = BoxBody(boundingBox)
                box.position[x, y] = z
                blockBoxes.add(box)
            }
        }
        return blockBoxes.toTypedArray<BoxBody>()
    }

    protected fun moveEntity(motion: Vec3f?): Vec3f? {
        if (blockBoxes == null) return null
        val collidedMove = getCollidedMotion(this, motion!!, *blockBoxes!!)
        position.add(collidedMove)
        return collidedMove
    }
}
