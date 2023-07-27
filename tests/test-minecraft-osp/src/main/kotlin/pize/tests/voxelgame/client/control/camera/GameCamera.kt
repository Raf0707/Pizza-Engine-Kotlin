package pize.tests.voxelgame.client.control.camera

import pize.Pize.dt
import pize.graphics.camera.PerspectiveCamera
import pize.math.Maths.clamp
import pize.math.Maths.floor
import pize.tests.voxelgame.client.ClientGame
import pize.tests.voxelgame.client.block.Blocks
import pize.tests.voxelgame.client.entity.LocalPlayer
import pize.tests.voxelgame.main.chunk.ChunkUtils
import pize.tests.voxelgame.main.chunk.LevelChunk
import pize.tests.voxelgame.main.chunk.storage.ChunkPos

class GameCamera(val game: ClientGame, near: Double, far: Double, fieldOfView: Double) :
    PerspectiveCamera(near, far, fieldOfView) {
    val player: LocalPlayer?
    private var target: CameraTarget?
    private val firstPerson: CameraTarget
    private val thirdPersonFront: CameraTarget
    private val thirdPersonBack: CameraTarget
    private var perspective: PerspectiveType
    private var notInterpolatedFov = 0f
    private var zoom = 1f
    var isInWater = false
        private set

    init {
        player = game.player
        firstPerson = FirstPersonPlayerCameraTarget(player)
        thirdPersonFront = ThirdPersonFrontCameraTarget(player)
        thirdPersonBack = ThirdPersonBackCameraTarget(player)
        perspective = PerspectiveType.FIRST_PERSON
        target = firstPerson
        setImaginaryOrigins(true, false, true)
    }

    override fun update() {
        // Follow target
        if (target == null) return
        val position = target.getPosition().copy()
        val rotation = target.getRotation().copy()

        // Shaking
        // final float playerSpeed = (float) player.getMotion().xz().len();
        // time += playerSpeed * 3;
        // final float shakingHorizontal = Mathc.sin(time) * 0.02F;
        // final float shakingVertical = Mathc.pow(Mathc.sin(time) * 0.02F, 2) - 0.02F;
        // final Vec2f shakingShift = new Vec2f(0, shakingHorizontal);
        // shakingShift.rotDeg(rotation.yaw);
        // position.add(shakingShift.x, shakingVertical, shakingShift.y);

        // Follow to target
        position.set(position)
        rotation.set(rotation)

        // Player
        val options = game.session.options
        var fov = options!!.fieldOfView / zoom
        if (player!!.isSprinting) fov *= 1.27f
        fov = fov

        // Interpolate FOV
        val currentFOV = fov
        super.fov = currentFOV + (notInterpolatedFov - currentFOV) * dt * 9
        super.update()

        // Is in water
        val block = game.level.getBlockProps(position.xf(), position.yf(), position.zf())
        isInWater = block.id == Blocks.WATER.id
    }

    fun setDistance(renderDistanceInChunks: Int) {
        // setFar(Math.max((renderDistanceInChunks + 0.5F) * SIZE, HEIGHT * 4));
    }

    fun getPerspective(): PerspectiveType {
        return perspective
    }

    fun setPerspective(perspective: PerspectiveType) {
        this.perspective = perspective
        target = when (perspective) {
            PerspectiveType.FIRST_PERSON -> firstPerson
            PerspectiveType.THIRD_PERSON_BACK -> thirdPersonBack
            PerspectiveType.THIRD_PERSON_FRONT -> thirdPersonFront
        }
    }

    fun getZoom(): Float {
        return zoom
    }

    fun setZoom(zoom: Float) {
        this.zoom = clamp(zoom, 1f, 200f)
    }

    override var fov: Float
        get() = super.fov
        set(fieldOfView) {
            notInterpolatedFov = fieldOfView
        }

    fun isChunkSeen(chunkX: Int, chunkZ: Int): Boolean {
        return frustum.isBoxInFrustum(
            (
                    chunkX * ChunkUtils.SIZE).toDouble(),
            0.0,
            (chunkZ * ChunkUtils.SIZE).toDouble(),
            (
                    chunkX * ChunkUtils.SIZE + ChunkUtils.SIZE).toDouble(),
            ChunkUtils.HEIGHT.toDouble(),
            (chunkZ * ChunkUtils.SIZE + ChunkUtils.SIZE
                    ).toDouble()
        )
    }

    fun isChunkSeen(pos: ChunkPos): Boolean {
        return isChunkSeen(pos.x, pos.z)
    }

    fun isChunkSeen(chunk: LevelChunk?): Boolean {
        val pos = chunk.getPosition()
        return frustum.isBoxInFrustum(
            (
                    pos!!.x * ChunkUtils.SIZE).toDouble(),
            0.0,
            (pos!!.z * ChunkUtils.SIZE).toDouble(),
            (
                    pos!!.x * ChunkUtils.SIZE + ChunkUtils.SIZE).toDouble(),
            ((chunk.getHighestSectionIndex() + 1) * ChunkUtils.SIZE).toDouble(),
            (pos!!.z * ChunkUtils.SIZE + ChunkUtils.SIZE
                    ).toDouble()
        )
    }

    fun chunkX(): Int {
        return floor((x / ChunkUtils.SIZE).toDouble())
    }

    fun chunkZ(): Int {
        return floor((z / ChunkUtils.SIZE).toDouble())
    }
}
