package pize.tests.minecraft.game.audio

import pize.tests.minecraft.game.options.SoundCategory

enum class Sound private constructor(val category: SoundCategory, val resourceId: String, val maxVolume: Float) {
    CLICK(SoundCategory.MASTER, "click", 0.25f)
}
