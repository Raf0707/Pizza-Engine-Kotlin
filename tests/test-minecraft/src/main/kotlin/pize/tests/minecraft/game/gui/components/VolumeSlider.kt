package pize.tests.minecraft.game.gui.components

import pize.math.Maths.round
import pize.tests.minecraft.game.Session
import pize.tests.minecraft.game.gui.text.Component
import pize.tests.minecraft.game.options.SoundCategory

class VolumeSlider(session: Session, private val soundCategory: SoundCategory) : Slider(session) {
    init {
        val initialVolume = session.options.getSoundVolume(soundCategory)
        text = Component().translation(
            soundCategory.translateKey,
            Component()
                .formattedText(round((initialVolume * 100).toDouble()).toString())
        )
        setValue(initialVolume.toDouble())
        setDivisions(100)
    }

    fun updateVolume() {
        if (!isChanged) return
        val volume = value
        text = Component().translation(
            soundCategory.translateKey,
            Component().formattedText(round((volume * 100).toDouble()).toString())
        )
        session.options.setSoundVolume(soundCategory, volume)
        session.options.save()
    }
}
