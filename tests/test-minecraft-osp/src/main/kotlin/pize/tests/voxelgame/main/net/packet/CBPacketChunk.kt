package pize.tests.voxelgame.main.net.packet

import pize.net.tcp.packet.IPacket
import pize.net.tcp.packet.PacketHandler
import pize.tests.voxelgame.client.chunk.ClientChunk
import pize.tests.voxelgame.client.level.ClientLevel
import pize.tests.voxelgame.main.chunk.LevelChunk
import pize.tests.voxelgame.main.chunk.LevelChunkSection
import pize.tests.voxelgame.main.chunk.storage.ChunkPos
import pize.tests.voxelgame.main.chunk.storage.Heightmap
import pize.tests.voxelgame.main.chunk.storage.HeightmapType
import pize.util.io.PizeInputStream
import pize.util.io.PizeOutputStream
import java.io.IOException
import java.util.*

class CBPacketChunk : IPacket<PacketHandler?> {
    private var position: ChunkPos? = null
    private var sections: Array<LevelChunkSection?>?
    private var highestSectionIndex = 0
    private var heightmapsToWrite: Collection<Heightmap?>? = null
    private var readHeightmaps: MutableMap<HeightmapType?, ShortArray?>? = null

    constructor() : super(PACKET_ID) {
        readHeightmaps = HashMap()
    }

    constructor(chunk: LevelChunk) : super(PACKET_ID) {
        position = chunk.position
        sections = chunk.sections
        highestSectionIndex = chunk.highestSectionIndex
        heightmapsToWrite = chunk.heightmaps
    }

    fun getChunk(level: ClientLevel): ClientChunk {
        val chunk = ClientChunk(level, position)
        chunk.setSections(sections!!, highestSectionIndex)
        chunk.setHeightmaps(readHeightmaps)
        return chunk
    }

    @Throws(IOException::class)
    override fun write(stream: PizeOutputStream?) {
        // Chunk position
        stream!!.writeInt(position!!.x)
        stream.writeInt(position!!.z)

        // Sections header
        val sectionsNum = Arrays.stream(sections).filter { obj: LevelChunkSection? -> Objects.nonNull(obj) }
            .count()
        stream.writeByte(sectionsNum.toByte().toInt())
        stream.writeByte(highestSectionIndex.toByte().toInt())

        // Sections data
        for (i in sections!!.indices) if (sections!![i] != null) writeSection(stream, i)

        // Heightmaps
        stream.writeByte(heightmapsToWrite!!.size.toByte().toInt())
        for (heightmap in heightmapsToWrite!!) writeHeightmap(stream, heightmap)
    }

    @Throws(IOException::class)
    private fun writeSection(stream: PizeOutputStream?, sectionIndex: Int) {
        val section = sections!![sectionIndex]
        stream!!.writeByte(sectionIndex) // index
        stream.writeShort(section!!.blocksNum) // blocks num
        stream.writeShortArray(section.blocks) // blocks data
        stream.writeByteArray(section.light) // light data
    }

    @Throws(IOException::class)
    private fun writeHeightmap(stream: PizeOutputStream?, heightmap: Heightmap?) {
        stream!!.writeByte(heightmap.getType().ordinal)
        stream.writeShortArray(heightmap.getValues())
    }

    @Throws(IOException::class)
    override fun read(stream: PizeInputStream?) {
        // Chunk position
        position = ChunkPos(
            stream!!.readInt(),
            stream.readInt()
        )

        // Sections header
        val sectionsNum = stream.readByte()
        highestSectionIndex = stream.readByte().toInt()
        if (sectionsNum.toInt() == 0) return
        sections = arrayOfNulls(16)

        // Sections data
        for (i in 0 until sectionsNum) readSection(stream)

        // Heightmaps
        val heightmapsNum = stream.readByte()
        for (i in 0 until heightmapsNum) readHeightmap(stream)
    }

    @Throws(IOException::class)
    private fun readSection(stream: PizeInputStream?) {
        val sectionIndex = stream!!.readByte() // index
        val blocksNum = stream.readShort() // blocks num
        val blocks = stream.readShortArray() // blocks data
        val light = stream.readByteArray() // light data
        val section = LevelChunkSection(blocks, light)
        section.blocksNum = blocksNum.toInt()
        sections!![sectionIndex.toInt()] = section
    }

    @Throws(IOException::class)
    private fun readHeightmap(stream: PizeInputStream?) {
        val type = HeightmapType.entries[stream!!.readByte().toInt()]
        val values = stream.readShortArray()
        readHeightmaps!![type] = values
    }

    companion object {
        const val PACKET_ID = 2
    }
}
