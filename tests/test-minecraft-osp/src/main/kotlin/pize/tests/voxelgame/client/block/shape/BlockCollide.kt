package pize.tests.voxelgame.client.block.shape

import pize.physic.BoundingBox3

class BlockCollide(vararg boxes: BoundingBox3) {
    val boxes: Array<BoundingBox3>

    init {
        this.boxes = boxes
    }

    companion object {
        val SOLID = BlockCollide(BoundingBox3(0.0, 0.0, 0.0, 1.0, 1.0, 1.0))
    }
}
