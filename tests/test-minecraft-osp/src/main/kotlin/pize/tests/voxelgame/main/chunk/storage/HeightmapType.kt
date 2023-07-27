package pize.tests.voxelgame.main.chunk.storage

import pize.tests.voxelgame.client.block.Blocks
import pize.tests.voxelgame.main.block.BlockData
import java.util.function.Predicate

enum class HeightmapType(val isOpaque: Predicate<Byte>) {
    HIGHEST(Predicate { blockID: Byte -> blockID == Blocks.AIR.id }),
    OPAQUE(Predicate { blockID: Byte -> BlockData.getProps(blockID)!!.isLightTranslucent }),
    SURFACE(Predicate { blockID: Byte -> blockID == Blocks.AIR.id || blockID == Blocks.WATER.id })
}
