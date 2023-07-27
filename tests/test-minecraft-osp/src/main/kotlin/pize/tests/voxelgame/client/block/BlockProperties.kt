package pize.tests.voxelgame.client.block

import pize.math.vecmath.vector.Vec3f.add
import pize.math.vecmath.vector.Vec3i.add
import pize.tests.voxelgame.client.block.model.BlockModel
import pize.tests.voxelgame.client.block.shape.BlockCollide
import pize.tests.voxelgame.client.block.shape.BlockCursor
import pize.tests.voxelgame.client.resources.GameResources
import pize.tests.voxelgame.main.Direction
import pize.tests.voxelgame.main.audio.BlockSoundPack
import pize.tests.voxelgame.main.block.BlockData
import pize.tests.voxelgame.main.chunk.ChunkUtils

abstract class BlockProperties protected constructor(ID: Int) {
    val iD: Byte
    val defaultData: Short

    /** Возвращает True если блок имеет форму стандартного вокселя
     * (куб, а не любая сложная модель)  */
    var isSolid = false
        protected set
    /** Возвращает уровень света блока  */
    var lightLevel = 0
        protected set

    /** Возвращает непрозрачность блока
     * (например: 0 - стекло, 15 - камень)  */
    var opacity = 0
        protected set
    /** Возвращает True если блок полупрозрачный  */
    var isTranslucent = false
        protected set
    /** Возвращает набор звуков для блока  */
    var soundPack: BlockSoundPack
        protected set
    /** Возвращает все возможные состояния блока  */
    var states: List<BlockState>
        protected set

    init {
        iD = ID.toByte()
        defaultData = BlockData.getData(ID)
        soundPack = BlockSoundPack.STONE
        states = ArrayList(1)
    }

    abstract fun load(resources: GameResources?)
    val isEmpty: Boolean
        /** Возвращает True если это воздух  */
        get() = iD == Blocks.AIR.id
    val isGlow: Boolean
        /** Возвращает True если блок является источником света  */
        get() = lightLevel != 0
    val isLightTranslucent: Boolean
        /** Возвращает True если блок пропускает свет  */
        get() = opacity != ChunkUtils.MAX_LIGHT_LEVEL
    val isLightTransparent: Boolean
        get() = opacity == 0

    /** Возвращает одно из состояний блока  */
    fun getState(index: Int): BlockState {
        return states[index]
    }

    protected fun newState(facing: Direction, model: BlockModel, collide: BlockCollide, cursor: BlockCursor) {
        states.add(states.size, BlockState(facing, model, collide, cursor))
    }

    override fun equals(`object`: Any?): Boolean {
        if (this === `object`) return true
        if (`object` == null || javaClass != `object`.javaClass) return false
        val blockProperties = `object` as BlockProperties
        return iD == blockProperties.iD
    }

    override fun hashCode(): Int {
        return iD.toInt()
    }
}
