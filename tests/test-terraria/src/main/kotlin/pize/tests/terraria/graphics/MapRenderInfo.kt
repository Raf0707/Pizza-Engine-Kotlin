package pize.tests.terraria.graphics

import pize.Pize.height
import pize.Pize.width

class MapRenderInfo {
    var cellSize = 0f
        private set
    var cellsPerWidth = 0f
        private set
    var cellsPerHeight = 0f
        private set
    var scale = 1f

    fun update() {
        val windowWidth = width
        val windowHeight = height
        cellSize =
            if (windowWidth > windowHeight) windowWidth.toFloat() / TILES_PER_SCREEN else windowHeight.toFloat() / TILES_PER_SCREEN
        cellsPerWidth = windowWidth / cellSize
        cellsPerHeight = windowHeight / cellSize
    }

    fun mulScale(scale: Double) {
        this.scale *= scale.toFloat()
    }

    companion object {
        const val TILES_PER_SCREEN = 30
    }
}
