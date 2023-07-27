package pize.graphics.camera.controller

import pize.Pize
import pize.math.util.EulerAngles

class Rotation3DController {
    @JvmField
    val rotation: EulerAngles
    var sensitivity: Float
    var isMouseShow = false
        private set
    private var nextFrameRotationLock: Boolean
    private var dAngX = 0f
    private var dAngY = 0f

    init {
        rotation = EulerAngles()
        sensitivity = 1f
        nextFrameRotationLock = true
        Pize.mouse()!!.show(isMouseShow)
    }

    fun update() {
        if (Pize.window()!!.isFocused && !isMouseShow) {
            if (!nextFrameRotationLock && Pize.mouse()!!.isInWindow) {
                val x = Pize.mouse()?.x?.toFloat()
                val y = Pize.mouse()?.y?.toFloat()
                dAngX += (Pize.width / 2f).toInt() - x!!
                dAngY += (Pize.height / 2f).toInt() - y!!
                rotation.yaw += (dAngX * 0.02 * sensitivity).toFloat()
                rotation.pitch += (dAngY * 0.02 * sensitivity).toFloat()
                rotation.limitPitch90()
                dAngX *= 0.1.toFloat()
                dAngY *= 0.1.toFloat()
            }
            Pize.mouse()!!.setPos(Pize?.width!! / 2, Pize?.height!! / 2)
            nextFrameRotationLock = false
        }
    }

    fun showMouse(showMouse: Boolean) {
        isMouseShow = showMouse
        Pize.mouse()!!.show(showMouse)
    }

    fun toggleShowMouse() {
        showMouse(!isMouseShow)
    }

    fun lockNextFrame() {
        nextFrameRotationLock = true
    }
}
