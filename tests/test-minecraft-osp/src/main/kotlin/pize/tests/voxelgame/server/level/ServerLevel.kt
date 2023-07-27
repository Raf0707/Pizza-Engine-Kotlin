package pize.tests.voxelgame.server.level

import pize.math.vecmath.vector.Vec3f
import pize.tests.voxelgame.client.block.Blocks
import pize.tests.voxelgame.main.audio.Sound
import pize.tests.voxelgame.main.chunk.ChunkUtils
import pize.tests.voxelgame.main.chunk.storage.HeightmapType
import pize.tests.voxelgame.main.level.Level
import pize.tests.voxelgame.main.net.packet.CBPacketPlaySound
import pize.tests.voxelgame.server.Server
import pize.tests.voxelgame.server.chunk.ServerChunk
import pize.tests.voxelgame.server.player.ServerPlayer

class ServerLevel(val server: Server) : Level() {
    override val chunkManager: ServerChunkManager
    override val configuration: ServerLevelConfiguration
    val lightEngine: LightEngine

    init {
        chunkManager = ServerChunkManager(this)
        configuration = ServerLevelConfiguration()
        lightEngine = LightEngine(this)
    }

    override fun getBlock(x: Int, y: Int, z: Int): Short {
        val targetChunk = getBlockChunk(x, z)
        return targetChunk?.getBlock(ChunkUtils.getLocalCoord(x), y, ChunkUtils.getLocalCoord(z))
            ?: Blocks.VOID_AIR.defaultData
    }

    override fun setBlock(x: Int, y: Int, z: Int, block: Short): Boolean {
        val targetChunk = getBlockChunk(x, z)
        return targetChunk?.setBlock(
            ChunkUtils.getLocalCoord(x),
            y,
            ChunkUtils.getLocalCoord(z),
            block
        )
            ?: false
    }

    override fun getHeight(x: Int, z: Int): Int {
        val targetChunk = getBlockChunk(x, z)
        return if (targetChunk != null) targetChunk.getHeightMap(HeightmapType.HIGHEST)!!
            .getHeight(ChunkUtils.getLocalCoord(x), ChunkUtils.getLocalCoord(z)) else 0
    }

    override fun getLight(x: Int, y: Int, z: Int): Byte {
        val targetChunk = getBlockChunk(x, z)
        return targetChunk?.getLight(ChunkUtils.getLocalCoord(x), y, ChunkUtils.getLocalCoord(z)) ?: 15
    }

    override fun setLight(x: Int, y: Int, z: Int, level: Int) {
        val targetChunk = getBlockChunk(x, z)
        targetChunk?.setLight(ChunkUtils.getLocalCoord(x), y, ChunkUtils.getLocalCoord(z), level)
    }

    val spawnPosition: Vec3f
        get() {
            val spawn = configuration.worldSpawn
            val spawnY = getHeight(spawn!!.xf(), spawn.yf()) + 1
            return Vec3f(spawn.x, spawnY.toFloat(), spawn.y)
        }

    fun playSound(sound: Sound?, volume: Float, pitch: Float, x: Float, y: Float, z: Float) {
        server.playerList.broadcastPacketLevel(
            this, CBPacketPlaySound(sound, volume, pitch, x, y, z)
        )
    }

    fun playSound(sound: Sound, volume: Float, pitch: Float, position: Vec3f) {
        server.playerList.broadcastPacketLevel(
            this, CBPacketPlaySound(sound, volume, pitch, position)
        )
    }

    fun playSoundExcept(
        except: ServerPlayer,
        sound: Sound?,
        volume: Float,
        pitch: Float,
        x: Float,
        y: Float,
        z: Float
    ) {
        server.playerList.broadcastPacketLevelExcept(
            this,
            CBPacketPlaySound(sound, volume, pitch, x, y, z),
            except
        )
    }

    override fun getBlockChunk(blockX: Int, blockZ: Int): ServerChunk? {
        return chunkManager.getChunk(ChunkUtils.getChunkPos(blockX), ChunkUtils.getChunkPos(blockZ))
    }
}
