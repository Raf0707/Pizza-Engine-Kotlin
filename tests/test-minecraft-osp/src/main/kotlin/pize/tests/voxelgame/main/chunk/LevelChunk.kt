package pize.tests.voxelgame.main.chunk

import pize.tests.voxelgame.client.block.BlockProperties
import pize.tests.voxelgame.client.block.Blocks
import pize.tests.voxelgame.main.block.BlockData
import pize.tests.voxelgame.main.chunk.storage.ChunkPos
import pize.tests.voxelgame.main.chunk.storage.Heightmap
import pize.tests.voxelgame.main.chunk.storage.HeightmapType
import pize.tests.voxelgame.main.level.Level
import kotlin.math.max

open class LevelChunk(val level: Level, val position: ChunkPos?) {
    val sections: Array<LevelChunkSection?>
    var highestSectionIndex: Int
        protected set
    protected val heightmaps: MutableMap<HeightmapType?, Heightmap>

    init {
        sections = arrayOfNulls(16)
        highestSectionIndex = -1

        // Heightmaps
        heightmaps = HashMap()
        for (type in HeightmapType.entries) heightmaps[type] = Heightmap(this, type)
    }

    fun getBlock(lx: Int, y: Int, lz: Int): Short {
        if (ChunkUtils.isOutOfBounds(lx, y, lz)) return Blocks.AIR.defaultData

        // Находим по Y нужную секцию
        val sectionIndex = ChunkUtils.getSectionIndex(y)
        val section = getSection(sectionIndex) ?: return Blocks.AIR.defaultData

        // Возвращаем блок
        return section.getBlock(lx, ChunkUtils.getLocalCoord(y), lz)
    }

    open fun setBlock(lx: Int, y: Int, lz: Int, blockState: Short): Boolean {
        if (ChunkUtils.isOutOfBounds(lx, y, lz)) return false

        // Проверяем является ли устанавливаемый блок воздухом
        val airID = Blocks.AIR.id.toInt()
        val blockID = BlockData.getID(blockState).toInt()
        val isBlockAir = blockID == airID

        // Находим по Y нужную секцию
        val sectionIndex = ChunkUtils.getSectionIndex(y)
        var section = getSection(sectionIndex)
        if (section != null) {
            // Если устанавливаемый блок воздух, и в секции остался один блок
            // - удаляем секцию, чтобы не загружать память секцией с 4096 блоками воздуха, и завершаем метод
            if (section.getBlocksNum() == 1 && isBlockAir && (getSection(sectionIndex - 1) == null || getSection(
                    sectionIndex + 1
                ) == null)
            ) {
                removeSection(sectionIndex)
                return true
            }

            // Если секция равна null
        } else {
            // Если устанавливаемый блок воздух - ничего не делаем и завершаем метод
            if (isBlockAir) return false

            // Если устанавливаемый блок не воздух - создаем секцию
            section = createSection(sectionIndex)
        }

        // Проверка на равенство устанавливаемого блока и текущего
        val ly = ChunkUtils.getLocalCoord(y)
        val oldBlockID = BlockData.getID(section.getBlock(lx, ly, lz)).toInt()
        if (blockID == oldBlockID) return false

        // Подсчет блоков в секции
        if (isBlockAir) section.blocksNum-- // Если блок был заменен воздухом - кол-во блоков в секции уменьшаем на 1
        else section.blocksNum++ // Если воздух был заменен блоком - увеличиваем на 1

        // УСТАНАВЛИВАЕМ БЛОК
        section.setBlock(lx, ly, lz, blockState)
        return true
    }

    fun getSection(index: Int): LevelChunkSection? {
        return sections[index]
    }

    protected fun removeSection(index: Int) {
        sections[index] = null
        if (highestSectionIndex != index) return
        for (i in sections.indices.reversed()) if (getSection(i) != null) {
            highestSectionIndex = i
            return
        }
        highestSectionIndex = -1
    }

    protected fun createSection(index: Int): LevelChunkSection {
        highestSectionIndex = max(highestSectionIndex.toDouble(), index.toDouble()).toInt()
        return LevelChunkSection().also { sections[index] = it }
    }

    fun setSections(sections: Array<LevelChunkSection?>, highestSectionIndex: Int) {
        System.arraycopy(sections, 0, this.sections, 0, sections.size)
        this.highestSectionIndex = highestSectionIndex
    }

    fun getBlockProps(lx: Int, y: Int, lz: Int): BlockProperties? {
        return BlockData.getProps(getBlock(lx, y, lz))
    }

    fun getBlockID(lx: Int, y: Int, lz: Int): Byte {
        return BlockData.getID(getBlock(lx, y, lz))
    }

    val isEmpty: Boolean
        get() = highestSectionIndex == -1

    fun getHeightmaps(): Collection<Heightmap> {
        return heightmaps.values
    }

    fun setHeightmaps(heightmaps: Map<HeightmapType, ShortArray>) {
        for ((key, value) in heightmaps) {
            val heightmap = Heightmap(this, key, value)
            this.heightmaps[heightmap.type] = heightmap
        }
    }

    fun getHeightMap(type: HeightmapType?): Heightmap? {
        return heightmaps[type]
    }

    fun getLight(lx: Int, y: Int, lz: Int): Byte {
        if (ChunkUtils.isOutOfBounds(y)) return 15

        // Находим по Y нужную секцию
        val sectionIndex = ChunkUtils.getSectionIndex(y)
        val section = getSection(sectionIndex) ?: return 15

        // Возвращаем блок
        return section.getLight(lx, ChunkUtils.getLocalCoord(y), lz)
    }

    open fun setLight(lx: Int, y: Int, lz: Int, level: Int) {
        // if(isOutOfBoundsE(lx, y, lz))
        //     return;

        // Находим по Y нужную секцию
        val sectionIndex = ChunkUtils.getSectionIndex(y)
        var section = getSection(sectionIndex)
        if (section == null) section = createSection(sectionIndex)
        section.setLight(lx, ChunkUtils.getLocalCoord(y), lz, level)
    }

    override fun equals(`object`: Any?): Boolean {
        if (`object` === this) return true
        if (`object` == null || `object`.javaClass != javaClass) return false
        val chunk = `object` as LevelChunk
        return position == chunk.position
    }

    override fun hashCode(): Int {
        return position.hashCode()
    }

    open fun getNeighbor(chunkX: Int, chunkZ: Int): LevelChunk? {
        return level.getChunkManager().getChunk(position!!.x + chunkX, position.z + chunkZ)
    }
}
