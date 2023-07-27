package pize.tests.voxelgame.main.block

import pize.tests.voxelgame.client.block.BlockProperties
import pize.tests.voxelgame.client.block.Blocks

object BlockData {
    fun getProps(data: Short): BlockProperties? {
        return Blocks.fromID(getID(data))
    }

    fun getProps(ID: Byte): BlockProperties? {
        return Blocks.fromID(ID)
    }

    fun getData(block: BlockProperties?, extraData: Byte): Short {
        return getData(block.getID().toInt(), extraData)
    }

    fun getData(id: Int, extraData: Byte): Short {
        return (id and 0xFF or (extraData.toShort().toInt() shl 8)).toShort()
    }

    fun getData(block: BlockProperties): Short {
        return getData(block.id.toInt())
    }

    fun getData(id: Int): Short {
        return getData(id, 0.toByte())
    }

    fun getID(data: Short): Byte {
        return (data.toInt() and 0xFF).toByte()
    }

    fun getState(data: Short): Byte {
        return (data.toInt() shr 8).toByte()
    }
}
