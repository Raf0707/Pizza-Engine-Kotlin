package pize.tests.minecraft.game.options

import pize.Pize.audio
import pize.Pize.monitor
import pize.Pize.window
import pize.audio.Audio.Companion.availableDevices
import pize.files.Resource
import pize.io.glfw.Key
import pize.tests.minecraft.game.Session
import pize.tests.minecraft.game.gui.screen.screens.VideoSettingsScreen
import java.util.*

class Options(private val session: Session, gameDirPath: String) {
    private val resOptions: Resource
    private val soundVolumes: MutableMap<SoundCategory?, Float>
    private val keyMappings: MutableMap<KeyMapping, Key?>
    var language: String? = "en_us"
    var fov = 90
    var renderDistance = 8
    var mipmapLevels = 1
    var maxFramerate = monitor()!!.refreshRate
        private set
    private var fullscreen = false
    private var showFps = false
    var mouseSensitivity = 0.5f
    private var audioDevice = audio()!!.current.name

    init {
        soundVolumes = HashMap()
        keyMappings = HashMap()
        resOptions = Resource(gameDirPath + "options.txt", true)
        resOptions.mkParentDirs()
        resOptions.create()
        init()
    }

    private fun init() {
        load()
        val availableAudioDevices = availableDevices
        if (availableAudioDevices != null && availableAudioDevices.contains(audioDevice)) audio()!!
            .getDevice(audioDevice)!!.makeCurrent() else {
            audioDevice = audio()!!.current.name
            save()
        }
        window()!!.setFullscreen(fullscreen)
        setMaxFramerate(maxFramerate, VideoSettingsScreen.Companion.MAX_SETTING_FRAMERATE)
    }

    private fun load() {
        val `in` = resOptions.reader
        while (`in`.hasNext()) {
            var parts = `in`.nextLine().split(" : ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            if (parts.size != 2) continue
            val value = parts[1]
            parts = parts[0].split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            if (parts.size != 2) continue
            val category = parts[0]
            val key = parts[1]
            try {
                when (category) {
                    "language" -> {
                        when (key) {
                            "code" -> language = value
                        }
                    }

                    "graphics" -> {
                        when (key) {
                            "fov" -> fov = value.toInt()
                            "renderDistance" -> renderDistance = value.toInt()
                            "mipmapLevels" -> mipmapLevels = value.toInt()
                            "maxFramerate" -> maxFramerate = value.toInt()
                            "fullscreen" -> fullscreen = value.toBoolean()
                            "showFps" -> showFps = value.toBoolean()
                        }
                    }

                    "key" -> keyMappings[KeyMapping.valueOf(key.uppercase(Locale.getDefault()))] =
                        Key.valueOf(value.uppercase(Locale.getDefault()))

                    "control" -> {
                        when (key) {
                            "mouseSensitivity" -> mouseSensitivity = value.toFloat()
                        }
                    }

                    "sound" -> soundVolumes[SoundCategory.valueOf(key.uppercase(Locale.getDefault()))] = value.toFloat()
                    "audio" -> {
                        when (key) {
                            "device" -> audioDevice = value
                        }
                    }
                }
            } catch (ignored: IllegalArgumentException) {
            }
        }
        `in`.close()
    }

    fun save() {
        val out = resOptions.writer
        out.println("language.code : $language")
        out.println("graphics.fov : $fov")
        out.println("graphics.renderDistance : $renderDistance")
        out.println("graphics.mipmapLevels : $mipmapLevels")
        out.println("graphics.maxFramerate : $maxFramerate")
        out.println("graphics.fullscreen : $fullscreen")
        out.println("graphics.showFps : $showFps")
        for (keyType in KeyMapping.entries) out.println(
            "key." + keyType.toString().lowercase(Locale.getDefault()) + " : " + keyMappings.getOrDefault(
                keyType,
                keyType.defaultKey
            ).toString().lowercase(Locale.getDefault())
        )
        out.println("control.mouseSensitivity : $mouseSensitivity")
        for (soundCategory in SoundCategory.entries) out.println(
            "sound." + soundCategory.toString().lowercase(Locale.getDefault()) + " : " + soundVolumes.getOrDefault(
                soundCategory,
                soundCategory.defaultVolume
            )
        )
        out.println("audio.device : $audioDevice")
        out.close()
    }

    fun getKey(keyType: KeyMapping): Key? {
        return keyMappings.getOrDefault(keyType, keyType.defaultKey)
    }

    fun setKey(keyType: KeyMapping, key: Key?) {
        keyMappings[keyType] = key
    }

    fun setMaxFramerate(maxFramerate: Int, unlimitedThreshold: Int) {
        this.maxFramerate = maxFramerate
        session.fpsLimiter.setFps(maxFramerate.toDouble())
        session.fpsLimiter.enable(maxFramerate > 0 && maxFramerate < unlimitedThreshold)
        window()!!.setVsync(maxFramerate == 0)
    }

    fun isFullscreen(): Boolean {
        return fullscreen
    }

    fun setFullscreen(fullscreen: Boolean) {
        this.fullscreen = fullscreen
        window()!!.setFullscreen(fullscreen)
    }

    fun isShowFps(): Boolean {
        return showFps
    }

    fun setShowFps(showFps: Boolean) {
        this.showFps = showFps
        session.ingameGui.showFps(showFps)
    }

    fun getSoundVolume(category: SoundCategory?): Float {
        return soundVolumes.getOrDefault(category, category.getDefaultVolume())
    }

    fun setSoundVolume(category: SoundCategory?, volume: Float) {
        soundVolumes[category] = volume
    }

    fun getAudioDevice(): String? {
        return audioDevice
    }

    fun setAudioDevice(audioDevice: String?) {
        this.audioDevice = audioDevice
        session.audioManager.setDevice(audioDevice)
    }
}
