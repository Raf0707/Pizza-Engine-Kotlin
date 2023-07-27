package pize.tests.voxelgame.client.level

import pize.Pize.execSync
import pize.tests.voxelgame.client.chunk.ClientChunk
import pize.tests.voxelgame.client.chunk.mesh.builder.ChunkBuilder
import pize.tests.voxelgame.main.chunk.ChunkUtils
import pize.tests.voxelgame.main.chunk.storage.ChunkPos
import pize.tests.voxelgame.main.level.ChunkManager
import pize.tests.voxelgame.main.level.ChunkManagerUtils
import pize.tests.voxelgame.main.net.packet.CBPacketChunk
import pize.tests.voxelgame.main.net.packet.SBPacketChunkRequest
import pize.util.time.PerSecCounter
import java.util.concurrent.*
import kotlin.concurrent.Volatile

class ClientChunkManager(val level: ClientLevel) : ChunkManager() {
    val tps: PerSecCounter
    val chunkBuilders: Array<ChunkBuilder?>
    private val executor: ExecutorService
    private var thread: Thread? = null

    @Volatile
    private var chunksLoadIsEnd = false
    private val requestedChunks: ConcurrentHashMap<ChunkPos?, Long>
    private val frontiers: CopyOnWriteArrayList<ChunkPos?>
    private val allChunks: ConcurrentHashMap<ChunkPos?, ClientChunk?>
    private val toBuildQueue: ConcurrentLinkedDeque<ClientChunk>
    private val freeBuilderIndex: Int
        private get() {
            for (i in chunkBuilders.indices) if (chunkBuilders[i]!!.state == 0) return i
            // else
            //     System.out.print(" " + i + ": " + chunkBuilders[i].getState());
            return -1
        }
    private val index = 0

    init {
        chunkBuilders = arrayOfNulls(BUILD_CHUNK_CORES)
        for (i in chunkBuilders.indices) chunkBuilders[i] = ChunkBuilder(this)
        executor = Executors.newScheduledThreadPool(BUILD_CHUNK_CORES) { runnable: Runnable? ->
            val thread = Thread(runnable)
            thread.setDaemon(true)
            thread.setPriority(Thread.MIN_PRIORITY)
            thread
        }
        requestedChunks = ConcurrentHashMap()
        frontiers = CopyOnWriteArrayList()
        allChunks = ConcurrentHashMap()
        toBuildQueue = ConcurrentLinkedDeque()
        tps = PerSecCounter()
    }

    private fun build(chunk: ClientChunk) {
        chunkBuilders[0]!!.build(chunk)

        // System.out.println("wait for index");
        // while(index == -1 || chunkBuilders[index].getState() != 0)
        //     index = getFreeBuilderIndex();
        // System.out.println(index);

        //final int finalIndex = index;
        //executor.execute(() -> {
        //    try{
        //        chunkBuilders[finalIndex].build(chunk);
        //    }catch(Exception e){
        //        System.out.println(e);
        //    }
        //});

        //index++;
        //if(index == BUILD_CHUNK_CORES)
        //    index = 0;
    }

    fun startLoadChunks() {
        thread = Thread({
            chunksLoadIsEnd = false
            while (!Thread.currentThread().isInterrupted) {
                tps.count()
                findChunks()
                buildChunks()
                checkChunks()
                Thread.yield()
            }
            chunksLoadIsEnd = true
        }, "ClientChunkManager-Thread")
        thread!!.setPriority(Thread.MIN_PRIORITY)
        thread!!.setDaemon(true)
        thread!!.start()
    }

    fun dispose() {
        executor.shutdownNow()
        try {
            executor.awaitTermination(5, TimeUnit.SECONDS)
        } catch (ignored: InterruptedException) {
        }
        thread!!.interrupt()
        while (!chunksLoadIsEnd);
        println("ENDED")
        clear()
    }

    fun reset() {
        dispose()
        clear()
        startLoadChunks()
    }

    fun clear() {
        toBuildQueue.clear()
        requestedChunks.clear()
        frontiers.clear()
        for (chunk in allChunks.values) chunk.getMeshStack().dispose()
        allChunks.clear()
    }

    fun reload() {
        dispose()
        clear()
        startLoadChunks()
    }

    private fun findChunks() {
        if (frontiers.size == 0) {
            val player = level.session.game.player
            putFrontier(
                ChunkPos(
                    ChunkUtils.getChunkPos(player!!.position.xf()),
                    ChunkUtils.getChunkPos(player.position.zf())
                )
            )
        }
        for (frontierPos in allChunks.keys) {
            ensureFrontier(frontierPos!!.getNeighbor(-1, 0))
            ensureFrontier(frontierPos.getNeighbor(1, 0))
            ensureFrontier(frontierPos.getNeighbor(0, -1))
            ensureFrontier(frontierPos.getNeighbor(0, 1))
        }
        frontiers.removeIf { chunkPos: ChunkPos? -> isOffTheGrid(chunkPos) }
    }

    private fun ensureFrontier(chunkPos: ChunkPos?) {
        if (frontiers.contains(chunkPos) || isOffTheGrid(chunkPos)) return
        putFrontier(chunkPos)
    }

    private fun putFrontier(frontierPos: ChunkPos?) {
        frontiers.add(frontierPos)
        if (!allChunks.containsKey(frontierPos) && !requestedChunks.containsKey(frontierPos) && toBuildQueue.stream()
                .noneMatch { chunk: ClientChunk -> chunk.position == frontierPos }
        ) {
            level.session.game.sendPacket(SBPacketChunkRequest(frontierPos!!.x, frontierPos.z))
            requestedChunks[frontierPos] = System.currentTimeMillis()
        }
    }

    private fun buildChunks() {
        while (!toBuildQueue.isEmpty()) {
            val chunk = toBuildQueue.poll() ?: continue
            if (isOffTheGrid(chunk.position)) continue
            build(chunk)
        }
    }

    fun checkChunks() {
        for (chunk in allChunks.values) {
            if (isOffTheGrid(chunk.getPosition())) unloadChunk(chunk)
        }
        for ((key, value) in requestedChunks) if (System.currentTimeMillis() - value > 2000) requestedChunks.remove(
            key
        )
    }

    fun receivedChunk(packet: CBPacketChunk) {
        val chunk = packet.getChunk(level)
        chunk!!.rebuild(false)
        allChunks[chunk.position] = chunk
        ChunkManagerUtils.rebuildNeighborChunks(chunk)
        requestedChunks.remove(chunk.position)
    }

    fun unloadChunk(chunk: ClientChunk?) {
        execSync { chunk.getMeshStack().dispose() }
        allChunks.remove(chunk.getPosition())
    }

    fun rebuildChunk(chunk: ClientChunk, important: Boolean) {
        if (!toBuildQueue.contains(chunk)) {
            if (important) toBuildQueue.addFirst(chunk) else toBuildQueue.addLast(chunk)
        }
    }

    override fun getChunk(chunkPos: ChunkPos?): ClientChunk? {
        return allChunks[chunkPos]
    }

    override fun getChunk(chunkX: Int, chunkZ: Int): ClientChunk? {
        return getChunk(ChunkPos(chunkX, chunkZ))
    }

    fun getAllChunks(): Collection<ClientChunk?> {
        return allChunks.values
    }

    private fun isOffTheGrid(chunkPos: ChunkPos?): Boolean {
        val player = level.session.game.player ?: return true
        return (ChunkManagerUtils.distToChunk(chunkPos!!.x, chunkPos.z, player.position)
                > level.session.options.renderDistance)
    }

    companion object {
        const val BUILD_CHUNK_CORES = 1
    }
}
