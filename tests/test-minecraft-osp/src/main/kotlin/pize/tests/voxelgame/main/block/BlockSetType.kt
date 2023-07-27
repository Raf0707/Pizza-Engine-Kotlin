package pize.tests.voxelgame.main.block

enum class BlockSetType {
    DESTROY,
    PLACE,
    REPLACE;

    companion object {
        fun from(oldBlockIsEmpty: Boolean, newBlockIsEmpty: Boolean): BlockSetType {
            if (oldBlockIsEmpty && !newBlockIsEmpty) return PLACE else if (newBlockIsEmpty && !oldBlockIsEmpty) return DESTROY
            return REPLACE
        }

        fun from(oldBlockState: Short, newBlockState: Short): BlockSetType {
            return from(
                BlockData.getID(oldBlockState).toInt() == 0,
                BlockData.getID(newBlockState).toInt() == 0
            )
        }

        fun from(oldBlockID: Byte, newBlockID: Byte): BlockSetType {
            return from(
                oldBlockID.toInt() == 0,
                newBlockID.toInt() == 0
            )
        }
    }
}
