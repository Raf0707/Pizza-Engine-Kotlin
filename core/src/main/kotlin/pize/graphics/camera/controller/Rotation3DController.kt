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
                val x = Pize.mouse().getX().toFloat()
                val y = Pize.mouse().getY().toFloat()
                dAngX += (Pize.getWidth() / 2f).toInt() - x
                dAngY += (Pize.getHeight() / 2f).toInt() - y
                rotation.yaw += (dAngX * 0.02 * sensitivity).toFloat()
                rotation.pitch += (dAngY * 0.02 * sensitivity).toFloat()
                rotation.limitPitch90()
                dAngX *= 0.1.toFloat()
                dAngY *= 0.1.toFloat()
            }
            Pize.mouse()!!.setPos(Pize.getWidth() / 2, Pize.getHeight() / 2)
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
