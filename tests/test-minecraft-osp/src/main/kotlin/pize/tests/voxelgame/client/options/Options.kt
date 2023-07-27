package pize.tests.voxelgame.client.options

import pize.Pize.window
import pize.files.Resource
import pize.io.glfw.Key
import pize.tests.voxelgame.Minecraft
import pize.tests.voxelgame.main.net.PlayerProfile
import java.util.*

class Options(private val session: Minecraft, gameDirPath: String?) {
    private val fileResource: Resource
    var host = DEFAULT_HOST
        private set
    var playerName: String = PlayerProfile.Companion.genFunnyName()
    private val keyMappings: MutableMap<KeyMapping, Key?>
    private var fieldOfView = DEFAULT_FIELD_OF_VIEW
    private var renderDistance = DEFAULT_RENDER_DISTANCE
    private var maxFrameRate = DEFAULT_MAX_FRAME_RATE // 0 - VSync; [1, 255]; 256 - Unlimited
    private var fullscreen = DEFAULT_FULLSCREEN
    var isShowFPS = DEFAULT_SHOW_FPS
    private var mouseSensitivity = DEFAULT_MOUSE_SENSITIVITY
    var brightness = DEFAULT_BRIGHTNESS
    var isFirstPersonModel = DEFAULT_FIRST_PERSON_MODEL

    init {
        keyMappings = HashMap()
        fileResource = Resource(gameDirPath + "options.txt", true)
        fileResource.create()
    }

    fun load() {
        val reader = fileResource.reader
        try {
            while (reader.hasNext()) {
                val parts = reader.nextLine().split(" : ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                if (parts.size != 2) continue
                val value = parts[1].trim { it <= ' ' }
                val keyParts = parts[0].split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                if (keyParts.size != 2) continue
                val category = keyParts[0]
                val key = keyParts[1]
                when (category) {
                    "remote" -> {
                        when (key) {
                            "host" -> setRemoteHost(value)
                        }
                    }

                    "player" -> {
                        when (key) {
                            "name" -> playerName = value
                            "firstPersonModel" -> isFirstPersonModel = value.toBoolean()
                        }
                    }

                    "graphics" -> {
                        when (key) {
                            "fieldOfView" -> setFieldOfView(value.toInt())
                            "renderDistance" -> setRenderDistance(value.toInt())
                            "maxFramerate" -> setMaxFrameRate(value.toInt())
                            "fullscreen" -> setFullscreen(value.toBoolean())
                            "showFps" -> isShowFPS = value.toBoolean()
                            "brightness" -> brightness = value.toFloat()
                        }
                    }

                    "key" -> setKey(
                        KeyMapping.valueOf(key.uppercase(Locale.getDefault())),
                        Key.valueOf(value.uppercase(Locale.getDefault()))
                    )

                    "control" -> {
                        when (key) {
                            "mouseSensitivity" -> setMouseSensitivity(value.toFloat())
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun save() {
        val out = fileResource.writer
        out.println("remote.host : $host")
        out.println("player.name : $playerName")
        out.println("player.firstPersonModel : " + isFirstPersonModel)
        out.println("graphics.fieldOfView : $fieldOfView")
        out.println("graphics.renderDistance : $renderDistance")
        out.println("graphics.maxFramerate : $maxFrameRate")
        out.println("graphics.fullscreen : $fullscreen")
        out.println("graphics.showFps : " + isShowFPS)
        out.println("graphics.brightness : $brightness")
        out.println("control.mouseSensitivity : $mouseSensitivity")
        for (keyType in KeyMapping.entries) out.println(
            "key." + keyType.toString().lowercase(Locale.getDefault()) + " : " + keyMappings.getOrDefault(
                keyType,
                keyType.default
            ).toString().lowercase(Locale.getDefault())
        )
        out.close()
    }

    fun setRemoteHost(host: String) {
        this.host = host
    }

    fun getKey(keyType: KeyMapping): Key? {
        return keyMappings.getOrDefault(keyType, keyType.default)
    }

    fun setKey(keyType: KeyMapping, key: Key?) {
        keyMappings[keyType] = key
    }

    fun getFieldOfView(): Int {
        return fieldOfView
    }

    fun setFieldOfView(fieldOfView: Int) {
        this.fieldOfView = fieldOfView
        val camera = session.game.camera
        if (camera != null) camera.fov = fieldOfView.toFloat()
    }

    fun getRenderDistance(): Int {
        return renderDistance
    }

    fun setRenderDistance(renderDistance: Int) {
        this.renderDistance = renderDistance
        val camera = session.game.camera
        camera?.setDistance(renderDistance)
    }

    fun getMaxFrameRate(): Int {
        return maxFrameRate
    }

    fun setMaxFrameRate(maxFrameRate: Int) {
        this.maxFrameRate = maxFrameRate
        session.fpsSync.setFps(maxFrameRate.toDouble())
        session.fpsSync.enable(maxFrameRate > 0 && maxFrameRate < UNLIMITED_FPS_THRESHOLD)
        window()!!.setVsync(maxFrameRate == 0)
    }

    fun isFullscreen(): Boolean {
        return fullscreen
    }

    fun setFullscreen(fullscreen: Boolean) {
        this.fullscreen = fullscreen
        window()!!.setFullscreen(fullscreen)
    }

    fun getMouseSensitivity(): Float {
        return mouseSensitivity
    }

    fun setMouseSensitivity(mouseSensitivity: Float) {
        this.mouseSensitivity = mouseSensitivity
        session.controller.playerController.rotationController.sensitivity = mouseSensitivity
    }

    companion object {
        var UNLIMITED_FPS_THRESHOLD = 256
        var DEFAULT_FIELD_OF_VIEW = 85
        var DEFAULT_RENDER_DISTANCE = 14
        var DEFAULT_MOUSE_SENSITIVITY = 1f
        var DEFAULT_BRIGHTNESS = 0.5f
        var DEFAULT_MAX_FRAME_RATE = 0 // VSync
        var DEFAULT_FULLSCREEN = false
        var DEFAULT_SHOW_FPS = false
        var DEFAULT_FIRST_PERSON_MODEL = false
        var DEFAULT_HOST = "0.0.0.0:22854"
    }
}
