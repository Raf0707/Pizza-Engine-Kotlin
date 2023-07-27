package pize.tests.minecraft.game

import pize.Pize.window
import pize.files.Resource
import pize.graphics.util.ScreenUtils.saveScreenshot
import pize.io.glfw.Key
import pize.tests.minecraft.game.audio.AudioManager
import pize.tests.minecraft.game.audio.MusicManager
import pize.tests.minecraft.game.gui.screen.ScreenManager
import pize.tests.minecraft.game.gui.screen.screens.*
import pize.tests.minecraft.game.lang.LanguageManager
import pize.tests.minecraft.game.options.KeyMapping
import pize.tests.minecraft.game.options.Options
import pize.tests.minecraft.game.resources.*
import pize.tests.minecraft.utils.log.Logger
import pize.util.time.Sync
import java.awt.Rectangle
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class Session : Renderer {
    val options: Options
    val languageManager: LanguageManager
    var resourceManager: ResourceManager? = null
    val musicManager: MusicManager
    val audioManager: AudioManager
    val screenManager: ScreenManager
    val ingameGui: IngameGUI
    val gameRenderer: GameRenderer
    val fpsLimiter: Sync

    init {
        Logger.Companion.instance().info("INIT:")
        fpsLimiter = Sync(0.0)
        options = Options(this, GAME_DIR_PATH)
        languageManager = LanguageManager()
        languageManager.updateAvailableLanguages()
        val info = languageManager.getLanguageInfo(options.language)
        languageManager.selectLanguage(if (info == null) "en_us" else info.code)
        run {
            resourceManager = ResourceManager()
            // TEXTURE: GUI
            resourceManager.setLocation("vanilla/textures/gui/")
            run {
                resourceManager!!.putTexture("button", "widgets.png", Rectangle(0, 66, 200, 20))
                resourceManager!!.putTexture("button_hover", "widgets.png", Rectangle(0, 86, 200, 20))
                resourceManager!!.putTexture("button_blocked", "widgets.png", Rectangle(0, 46, 200, 20))
                resourceManager!!.putTexture("options_background", "options_background.png")
                resourceManager!!.putTexture("title_left_part", "title/minecraft.png", Rectangle(0, 0, 155, 44))
                resourceManager!!.putTexture("title_right_part", "title/minecraft.png", Rectangle(0, 45, 119, 44))
                resourceManager!!.putTexture("title_edition", "title/edition.png", Rectangle(0, 0, 98, 14))
                resourceManager!!.putTexture("title_edition", "title/edition.png", Rectangle(0, 0, 98, 14))
                resourceManager!!.putTexture("panorama_overlay", "title/background/panorama_overlay.png")
            }
            // FONT
            resourceManager.setLocation("")
            run { resourceManager!!.putFontFnt("font_minecraft", "vanilla/font/default.fnt") }
            // MUSIC
            resourceManager.setLocation("music/menu/")
            run {
                resourceManager!!.putMusic("Beginning 2", "beginning2.ogg")
                resourceManager!!.putMusic("Moog City 2", "moogcity2.ogg")
                resourceManager!!.putMusic("Floating Trees", "floatingtrees.ogg")
                resourceManager!!.putMusic("Mutation", "mutation.ogg")
            }
            // SOUND
            resourceManager.setLocation("sound/")
            run { resourceManager!!.putSound("click", "random/click.ogg") }
            // LOAD
            resourceManager.load()
        }
        Logger.Companion.instance().info("Init Sound")
        musicManager = MusicManager(this)
        audioManager = AudioManager(this)
        Logger.Companion.instance().info("Init GUI")
        screenManager = ScreenManager()
        screenManager.putScreen("main_menu", MainMenuScreen(this))
        screenManager.putScreen("world_selection", WorldSelectionScreen(this))
        screenManager.putScreen("options", OptionsScreen(this))
        screenManager.putScreen("video_settings", VideoSettingsScreen(this))
        screenManager.putScreen("audio_settings", AudioSettingsScreen(this))
        screenManager.setCurrentScreen("main_menu")
        Logger.Companion.instance().info("Init Renderer")
        ingameGui = IngameGUI(this)
        gameRenderer = GameRenderer(this)
        Logger.Companion.instance().info("RENDERING:")
    }

    override fun render() {
        fpsLimiter.sync()
        gameRenderer.render()
        if (Key.R.isDown) resourceManager!!.reload()
        if (options.getKey(KeyMapping.FULLSCREEN)!!.isDown) window()!!.toggleFullscreen()
        if (options.getKey(KeyMapping.SCREENSHOT)!!.isDown) takeScreenshot()
    }

    override fun resize(width: Int, height: Int) {
        gameRenderer.resize(width, height)
        screenManager.resize(width, height)
        ingameGui.resize(width, height)
    }

    override fun dispose() {
        gameRenderer.dispose()
        resourceManager!!.dispose()
        screenManager.dispose()
        musicManager.dispose()
        audioManager.dispose()
    }

    private fun takeScreenshot() {
        val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_hh.mm.ss")
        val date = dateTimeFormatter.format(ZonedDateTime.now())
        val resource = Resource(GAME_DIR_PATH + "screenshots/" + date + ".png")
        resource.mkDirs()
        saveScreenshot(resource.path)
    }

    companion object {
        val GAME_DIR_PATH = System.getProperty("user.home") + "/minecraft/"
    }
}
