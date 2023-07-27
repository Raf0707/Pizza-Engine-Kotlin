package pize.tests.voxelgame.server.level

import pize.tests.voxelgame.main.chunk.ChunkUtils
import pize.tests.voxelgame.main.chunk.storage.ChunkPos
import pize.tests.voxelgame.main.chunk.storage.HeightmapType
import pize.tests.voxelgame.main.level.ChunkManager
import pize.tests.voxelgame.main.level.ChunkManagerUtils
import pize.tests.voxelgame.main.net.packet.CBPacketChunk
import pize.tests.voxelgame.server.chunk.ServerChunk
import pize.tests.voxelgame.server.player.ServerPlayer
import pize.util.time.PerSecCounter
import java.util.*
import java.util.concurrent.*

class ServerChunkManager(val level: ServerLevel) : ChunkManager() {
    private val requestedChunks: MutableMap<ChunkPos?, ServerPlayer>
    private val newFrontiers: CopyOnWriteArrayList<ChunkPos?>
    private val frontiers: CopyOnWriteArrayList<ChunkPos?>
    private val processChunks: MutableMap<ChunkPos?, ServerChunk>
    private val allChunks: MutableMap<ChunkPos?, ServerChunk?>
    private val loadQueue: Queue<ChunkPos?>
    val tps: PerSecCounter
    private val executorService: ExecutorService

    init {
        requestedChunks = HashMap()
        frontiers = CopyOnWriteArrayList()
        newFrontiers = CopyOnWriteArrayList()
        loadQueue = LinkedBlockingQueue()
        processChunks = ConcurrentHashMap()
        allChunks = ConcurrentHashMap()
        tps = PerSecCounter()
        executorService = Executors.newSingleThreadExecutor { runnable: Runnable? ->
            val thread = Thread(runnable, "ServerChunkManager-Thread")
            thread.setPriority(Thread.MIN_PRIORITY)
            thread.setDaemon(true)
            thread
        }
    }

    fun start() {
        executorService.submit {
            while (!Thread.interrupted()) {
                tps.count()
                try {
                    findChunks()
                    loadChunks()
                    unloadChunks()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                Thread.yield()
            }
        }
    }

    fun addToAllChunks(chunk: ServerChunk) {
        allChunks[chunk.position] = chunk
        sendChunkIsRequired(chunk)
    }

    fun sendChunkIsRequired(chunk: ServerChunk) {
        if (requestedChunks.containsKey(chunk.position)) {
            requestedChunks[chunk.position]!!.sendPacket(CBPacketChunk(chunk))
            requestedChunks.remove(chunk.position)
        }
    }

    fun requestedChunk(player: ServerPlayer, chunkPos: ChunkPos?) {
        val chunk = allChunks[chunkPos]
        if (chunk != null) player.sendPacket(CBPacketChunk(chunk)) else requestedChunks[chunkPos] = player
    }

    fun loadInitChunkForPlayer(player: ServerPlayer) {
        val chunkPos = ChunkPos(
            ChunkUtils.getChunkPos(player.position.xf()),
            ChunkUtils.getChunkPos(player.position.zf())
        )
        ensureFrontier(chunkPos)
    }

    private fun findChunks() {
        // Load spawn chunks
        if (frontiers.size == 0) {
            val spawn = level.configuration.worldSpawn
            ensureFrontier(
                ChunkPos(
                    ChunkUtils.getChunkPos(spawn!!.xf()),
                    ChunkUtils.getChunkPos(spawn.yf())
                )
            )
        }

        // Load players chunks
        for (player in level.server.playerList.players) {
            ensureFrontier(
                ChunkPos(
                    ChunkUtils.getChunkPos(player!!.position.xf()),
                    ChunkUtils.getChunkPos(player.position.zf())
                )
            )
        }

        // Fast flood fill
        for (frontierPos in frontiers) {
            ensureFrontier(frontierPos!!.getNeighbor(-1, 0))
            ensureFrontier(frontierPos.getNeighbor(1, 0))
            ensureFrontier(frontierPos.getNeighbor(0, -1))
            ensureFrontier(frontierPos.getNeighbor(0, 1))
        }
        frontiers.removeIf { chunkPos: ChunkPos? -> this.isOffTheGrid(chunkPos) }
        newFrontiers.removeIf { chunkPos: ChunkPos? -> this.isOffTheGrid(chunkPos) }
        if (newFrontiers.size == 0) return

        // Load new chunks
        loadQueue.addAll(newFrontiers)
        newFrontiers.clear()
    }

    private fun ensureFrontier(chunkPos: ChunkPos?) {
        if (frontiers.contains(chunkPos) || isOffTheGrid(chunkPos)) return
        frontiers.add(chunkPos)
        newFrontiers.add(chunkPos)
    }

    private fun loadChunks() {
        // Load
        for (chunkPos in loadQueue) {
            loadQueue.remove(chunkPos)
            if (isOffTheGrid(chunkPos, 2)) continue

            // Load / Generate
            // ServerChunk chunk = loadChunk(chunkPos);
            // if(chunk == null)
            val chunk = generateChunk(chunkPos) ?: continue // Generate

            // Update skylight
            chunk.getHeightMap(HeightmapType.OPAQUE)!!.update()
            chunk.level.lightEngine.updateSkyLight(chunk)

            // Add to list
            addToAllChunks(chunk)
        }
    }

    fun unloadChunks() {
        for (chunk in processChunks.values) if (isOffTheGrid(chunk.position, 2)) unloadChunk(chunk)
    }

    fun loadChunk(chunkPos: ChunkPos?): ServerChunk? {
        return null
    }

    fun generateChunk(chunkPos: ChunkPos?): ServerChunk? {
        val generator = level.configuration.generator ?: return null

        // Generate
        val chunk = generateChunkBase(chunkPos)

        // Base-Generate Neighbor Chunks
        generateChunkBase(chunkPos!!.getNeighbor(+1, +0))
        generateChunkBase(chunkPos.getNeighbor(+1, +1))
        generateChunkBase(chunkPos.getNeighbor(+1, -1))
        generateChunkBase(chunkPos.getNeighbor(-1, +0))
        generateChunkBase(chunkPos.getNeighbor(-1, +1))
        generateChunkBase(chunkPos.getNeighbor(-1, -1))
        generateChunkBase(chunkPos.getNeighbor(+0, +1))
        generateChunkBase(chunkPos.getNeighbor(+0, -1))

        // Generate Decorations
        generator.generateDecorations(chunk)
        return chunk
    }

    fun generateChunkBase(chunkPos: ChunkPos?): ServerChunk? {
        // If Generated
        if (processChunks.containsKey(chunkPos)) return processChunks[chunkPos]
        if (allChunks.containsKey(chunkPos)) return allChunks[chunkPos]

        // Generate Base
        val chunk = ServerChunk(level, chunkPos)
        processChunks[chunk.position] = chunk
        val generator = level.configuration.generator
        generator!!.generate(chunk)
        return chunk
    }

    fun unloadChunk(chunk: ServerChunk) {
        processChunks.remove(chunk.position)
    }

    override fun getChunk(chunkPos: ChunkPos?): ServerChunk? {
        return allChunks.getOrDefault(chunkPos, processChunks[chunkPos])
    }

    override fun getChunk(chunkX: Int, chunkZ: Int): ServerChunk? {
        return getChunk(ChunkPos(chunkX, chunkZ))
    }

    private fun isOffTheGrid(chunkPos: ChunkPos?): Boolean {
        return isOffTheGrid(chunkPos, 0)
    }

    private fun isOffTheGrid(chunkPos: ChunkPos?, renderDistanceIncrease: Int): Boolean {
        val spawn = level.server.levelManager.defaultLevel.configuration.worldSpawn
        if (ChunkManagerUtils.distToChunk(
                chunkPos!!.x,
                chunkPos.z,
                spawn
            ) <= level.server.configuration.maxRenderDistance + renderDistanceIncrease
        ) return false
        for (entity in level.entities) if (entity is ServerPlayer) if (ChunkManagerUtils.distToChunk(
                chunkPos.x, chunkPos.z, entity.position
            ) <= entity.renderDistance + renderDistanceIncrease
        ) return false
        return true
    }
}
