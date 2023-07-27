package pize.graphics.postprocess

import pize.app.Disposable
import pize.app.Resizable

interface PostProcessEffect : Disposable, Resizable {
    fun begin()
    fun end()
    fun end(target: PostProcessEffect)
}
