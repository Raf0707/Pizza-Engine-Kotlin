package pize.gui.components

class RegionMesh(
    beginX: Float,
    beginY: Float,
    separatorX1: Float,
    separatorY1: Float,
    separatorX2: Float,
    separatorY2: Float,
    endX: Float,
    endY: Float
) {
    val mesh: FloatArray

    init {
        mesh = floatArrayOf(
            beginX,
            separatorX1,
            separatorX2,
            endX,
            beginY,
            separatorY1,
            separatorY2,
            endY
        )
    }
}
