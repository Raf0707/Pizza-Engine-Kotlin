package pize.tests.audio

import pize.Pize.dt
import pize.app.Disposable
import pize.audio.sound.Sound
import pize.graphics.gl.Gl.clearColor
import pize.graphics.gl.Gl.clearColorBuffer
import pize.graphics.texture.Texture
import pize.graphics.util.batch.TextureBatch
import pize.gui.Align
import pize.gui.LayoutType
import pize.gui.components.*
import pize.gui.constraint.Constraint.Companion.aspect
import pize.gui.constraint.Constraint.Companion.relative

class AudioPlayerUI : Disposable {
    private val batch: TextureBatch
    private val sliderBarTexture: Texture
    private val sliderHandleTexture: Texture
    private val layout: Layout
    private var sound: Sound? = null
    private fun createUI() {
        // Background and Handle for Sliders
        val sliderBackgroundTextureMesh = RegionMesh(0f, 0f, 3f, 4f, 93f, 20f, 96f, 24f)
        val sliderBackground = NinePatchImage(sliderBarTexture, sliderBackgroundTextureMesh)
        sliderBackground.expandType = ExpandType.HORIZONTAL
        val sliderHandle = Image(sliderHandleTexture)
        sliderHandle.setSize(aspect(sliderHandleTexture.aspect().toDouble()), relative(1.0))

        // Slider Position
        val positionSlider = Slider(sliderBackground, sliderHandle)
        positionSlider.setSize(relative(1.0), relative(0.5))
        layout.put("position", positionSlider)

        // Slider Pitch
        val pitchSlider = Slider(sliderBackground.copy(), sliderHandle.copy())
        pitchSlider.setSize(relative(1.0), relative(0.5))
        pitchSlider.setValue((1 / 2f).toDouble())
        layout.put("pitch", pitchSlider)
    }

    fun update() {
        updateSound()

        // Render
        clearColor(1f, 1f, 1f, 1f)
        clearColorBuffer()
        batch.begin()
        layout.render(batch)
        batch.end()
    }

    private var currentPosition = 0f

    init {
        batch = TextureBatch()
        sliderBarTexture = Texture("slider_bar.png")
        sliderHandleTexture = Texture("slider_handle.png")
        layout = Layout()
        layout.setLayoutType(LayoutType.VERTICAL)
        layout.alignItems(Align.UP)
        createUI()
    }

    private fun updateSound() {
        val positionSlider = layout.get<Slider>("position")
        val pitchSlider = layout.get<Slider>("pitch")
        val sliderPosition = positionSlider.value

        // Pause while setting position
        if (positionSlider.isTouchDown) sound!!.pause() else if (positionSlider.isTouchReleased && sliderPosition != 1f) sound!!.play()
        if (sound!!.isPlaying || sound!!.isPaused) {
            // Set new sound position
            if (currentPosition != sliderPosition) sound!!.setPosition((sliderPosition * sound!!.duration).toDouble())

            // Increase slider position
            currentPosition = positionSlider.value + 1f / sound!!.duration * dt * sound!!.pitch
            positionSlider.setValue(currentPosition.toDouble())
        }

        // Set new pitch
        if (pitchSlider.isTouched && pitchSlider.isChanged) {
            sound!!.setPitch((pitchSlider.value * 2).toDouble())
            sound!!.setPosition((currentPosition * sound!!.duration).toDouble())
        }
    }

    fun setSound(sound: Sound?) {
        if (this.sound != null) this.sound!!.stop()
        this.sound = sound
    }

    fun play() {
        if (sound == null) return
        sound!!.play()
    }

    override fun dispose() {
        sliderBarTexture.dispose()
        sliderHandleTexture.dispose()
    }
}
