package pize.tests.terraria.graphics

import pize.graphics.camera.Camera2D
import pize.graphics.camera.CenteredOrthographicCamera
import pize.graphics.texture.Texture
import pize.graphics.texture.TextureRegion
import pize.graphics.util.batch.TextureBatch
import pize.math.Maths.ceil
import pize.math.Maths.floor
import pize.tests.terraria.entity.Entity
import pize.tests.terraria.map.WorldMap
import pize.tests.terraria.tile.TileType
import kotlin.math.max
import kotlin.math.min

class GameRenderer {
    val renderInfo: MapRenderInfo
    private val mapBatch: TextureBatch
    private val entityBatch: TextureBatch
    private val camera: CenteredOrthographicCamera
    private val tileTexture: TextureRegion

    init {
        renderInfo = MapRenderInfo()
        mapBatch = TextureBatch(100000)
        entityBatch = TextureBatch(500)
        camera = CenteredOrthographicCamera()
        tileTexture = TextureRegion(Texture("texture/Tiles_2.png"), 18 * 1, 18 * 1, 16, 16)
    }

    fun update() {
        renderInfo.update()
        camera.scale = renderInfo.cellSize * renderInfo.scale
        camera.update()
    }

    fun renderMap(map: WorldMap?) {
        val camHalfWidth = renderInfo.cellsPerWidth / 2 / renderInfo.scale
        val camHalfHeight = renderInfo.cellsPerHeight / 2 / renderInfo.scale
        var beginX = floor((camera.x - camHalfWidth).toDouble())
        var beginY = floor((camera.y - camHalfHeight).toDouble())
        var endX = ceil((camera.x + camHalfWidth).toDouble())
        var endY = ceil((camera.y + camHalfHeight).toDouble())
        beginX = max(beginX.toDouble(), 0.0).toInt()
        beginY = max(beginY.toDouble(), 0.0).toInt()
        endX = min(endX.toDouble(), map.getWidth().toDouble()).toInt()
        endY = min(endY.toDouble(), map.getHeight().toDouble()).toInt()
        mapBatch.begin(camera)
        for (i in beginX until endX) for (j in beginY until endY) {
            val tile = map!!.getTile(i, j)
            if (tile != null && tile.type != TileType.AIR) mapBatch.draw(tileTexture, i.toFloat(), j.toFloat(), 1f, 1f)
        }
        mapBatch.end()
    }

    fun renderEntities(entities: Iterable<Entity?>?) {
        entityBatch.begin(camera)
        for (entity in entities!!) entity!!.render(entityBatch)
        entityBatch.end()
    }

    fun getCamera(): Camera2D {
        return camera
    }
}
