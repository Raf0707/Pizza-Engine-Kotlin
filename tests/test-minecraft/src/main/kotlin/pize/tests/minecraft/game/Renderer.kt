package pize.tests.minecraft.game

import pize.app.Disposable
import pize.app.Resizable

interface Renderer : Disposable, Resizable {
    fun render()
}
