package pize.tests.audio

import pize.Pize.create
import pize.Pize.exit
import pize.Pize.run
import pize.Pize.window
import pize.app.AppAdapter
import pize.audio.io.WavFile
import pize.audio.sound.Sound
import pize.audio.util.SoundGenerator
import pize.files.Resource
import pize.io.glfw.Key

class Main : AppAdapter() {
    private var sound: Sound? = null
    private var AudioPlayerUI: AudioPlayerUI? = null
    override fun init() {
        val resource = Resource("Generated.wav", true)
        resource.create()

        // Generate WAV File and Save
        val generator = SoundGenerator()
        val wavFile = WavFile(resource.file, generator.sampleRate, generator.channels)
        wavFile.setData(generator.sinDown(880.0, 0.15))
        wavFile.save()

        // Load Generated WAV
        sound = Sound(resource)
        println("Generated.wav duration: " + sound!!.duration + " sec")
        sound!!.setVolume(0.25)
        sound!!.play()
        while (sound!!.isPlaying);
        sound!!.dispose()

        // Load OGG
        sound = Sound("MyMusic.ogg")
        println("MyMusic.ogg duration: " + sound!!.duration + " sec")

        // Play audio with GUI
        AudioPlayerUI = AudioPlayerUI()
        AudioPlayerUI!!.setSound(sound)
        AudioPlayerUI!!.play()
    }

    override fun render() {
        if (Key.ESCAPE.isDown) exit()
        AudioPlayerUI!!.update()
    }

    override fun dispose() {
        sound!!.dispose()
        AudioPlayerUI!!.dispose()
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            create("Audio", 700, 150)
            window()!!.setIcon("icon.png")
            run(Main())
        }
    }
}
