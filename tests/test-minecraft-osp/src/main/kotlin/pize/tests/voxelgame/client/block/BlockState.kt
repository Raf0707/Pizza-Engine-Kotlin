package pize.tests.voxelgame.client.block

import pize.tests.voxelgame.client.block.model.BlockModel
import pize.tests.voxelgame.client.block.shape.BlockCollide
import pize.tests.voxelgame.client.block.shape.BlockCursor
import pize.tests.voxelgame.main.Direction

class BlockState(
    /** Возвращает поворот блока  */
    val facing: Direction,
    /** Возвращает модель блока  */
    val model: BlockModel,
    /** Возвращает форму блока для коллизии  */
    val collide: BlockCollide, private val cursor: BlockCursor
) {
    /** Возвращает форму блока для курсора  */
    fun getCursor(): BlockCursor? {
        return cursor
    }
}
