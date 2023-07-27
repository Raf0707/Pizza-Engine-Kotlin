package pize.tests.voxelgame.client.control

import pize.math.Mathc.signum
import pize.math.Maths.abs
import pize.math.Maths.frac
import pize.math.vecmath.vector.Vec3f
import pize.math.vecmath.vector.Vec3i
import pize.physic.Ray3f
import pize.tests.voxelgame.Minecraft
import pize.tests.voxelgame.client.block.BlockProperties
import pize.tests.voxelgame.client.block.Blocks
import pize.tests.voxelgame.client.level.ClientLevel
import pize.tests.voxelgame.main.Direction
import pize.tests.voxelgame.main.block.BlockData
import pize.tests.voxelgame.main.chunk.ChunkUtils
import kotlin.math.max
import kotlin.math.min

class BlockRayCast(val session: Minecraft, length: Float) {
    private val ray: Ray3f
    private var length = 0f
    val selectedBlockPosition: Vec3i
    val imaginaryBlockPosition: Vec3i
    var selectedBlockProps: BlockProperties? = null
        private set
    var selectedBlockState: Byte = 0
        private set
    var selectedFace: Direction? = null
        private set
    var isSelected = false
        private set
    private var level: ClientLevel? = null

    init {
        ray = Ray3f()
        setLength(length)
        selectedBlockPosition = Vec3i()
        imaginaryBlockPosition = Vec3i()
    }

    fun setLevel(level: ClientLevel?) {
        this.level = level
    }

    fun update() {
        if (level == null) return

        // Update ray
        val player = session.game.player
        ray.direction.set(player.rotation.direction)
        ray.origin.set(player.lerpPosition.copy().add(0f, player.eyeHeight, 0f))

        // Get pos, dir, len
        val pos = ray.origin
        val dir = ray.direction

        // ...
        val step = Vec3i(
            signum(dir.x),
            signum(dir.y),
            signum(dir.z)
        )
        val delta = Vec3f(
            step.x / dir.x,
            step.y / dir.y,
            step.z / dir.z
        )
        val tMax = Vec3f(
            min((length / 2).toDouble(), abs((max(step.x.toDouble(), 0.0) - frac(pos.x)) / dir.x).toDouble()),
            min((length / 2).toDouble(), abs((max(step.y.toDouble(), 0.0) - frac(pos.y)) / dir.y).toDouble()),
            min((length / 2).toDouble(), abs((max(step.z.toDouble(), 0.0) - frac(pos.z)) / dir.z).toDouble())
        )
        selectedBlockPosition[pos.xf(), pos.yf()] = pos.zf()
        val faceNormal = Vec3i()
        isSelected = false
        while (tMax.len() < length) {
            if (tMax.x < tMax.y) {
                if (tMax.x < tMax.z) {
                    tMax.x += delta.x
                    selectedBlockPosition.x += step.x
                    faceNormal[-step.x, 0] = 0
                } else {
                    tMax.z += delta.z
                    selectedBlockPosition.z += step.z
                    faceNormal[0, 0] = -step.z
                }
            } else {
                if (tMax.y < tMax.z) {
                    tMax.y += delta.y
                    selectedBlockPosition.y += step.y
                    faceNormal[0, -step.y] = 0
                } else {
                    tMax.z += delta.z
                    selectedBlockPosition.z += step.z
                    faceNormal[0, 0] = -step.z
                }
            }
            if (selectedBlockPosition.y < 0 || selectedBlockPosition.y > ChunkUtils.HEIGHT_IDX) break
            val blockData = level!!.getBlock(selectedBlockPosition.x, selectedBlockPosition.y, selectedBlockPosition.z)
            val blockState = BlockData.getState(blockData)
            val block = BlockData.getProps(blockData)
            if (!block.isEmpty && block.id != Blocks.VOID_AIR.id && block!!.getState(blockState.toInt()).cursor != null) {
                if (block.isSolid) {
                    selectedFace = Direction.Companion.fromNormal(faceNormal.x, faceNormal.y, faceNormal.z)
                    selectedBlockProps = block
                    selectedBlockState = blockState
                    imaginaryBlockPosition.set(selectedBlockPosition).add(selectedFace.getNormal())
                    isSelected = true
                    break
                } else {
                    val shape = block.getState(blockState.toInt()).cursor
                    val vertices = shape.mesh.indexedVertices
                    if (ray.intersects(vertices)) {
                        selectedFace = Direction.Companion.fromNormal(faceNormal.x, faceNormal.y, faceNormal.z)
                        selectedBlockProps = block
                        selectedBlockState = blockState
                        imaginaryBlockPosition.set(selectedBlockPosition).add(selectedFace.getNormal())
                        isSelected = true
                        break
                    }
                }
            }
        }
    }

    fun setLength(length: Float) {
        this.length = length
    }
}
