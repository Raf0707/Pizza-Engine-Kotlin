package pize.tests.minecraft.game.gui.screen.screens

import pize.Pize.dt
import pize.Pize.exit
import pize.Pize.height
import pize.Pize.width
import pize.files.Resource
import pize.graphics.camera.PerspectiveCamera
import pize.graphics.texture.Texture
import pize.graphics.util.SkyBox
import pize.graphics.util.batch.TextureBatch
import pize.graphics.util.color.ImmutableColor
import pize.gui.Align
import pize.gui.LayoutType
import pize.gui.constraint.Constraint.Companion.aspect
import pize.gui.constraint.Constraint.Companion.pixel
import pize.gui.constraint.Constraint.Companion.relative
import pize.tests.minecraft.game.Session
import pize.tests.minecraft.game.gui.components.*
import pize.tests.minecraft.game.gui.constraints.GapConstraint
import pize.tests.minecraft.game.gui.screen.Screen
import pize.tests.minecraft.game.gui.text.Component
import pize.tests.minecraft.game.gui.text.formatting.StyleFormatting

class MainMenuScreen(session: Session) : Screen(session) {
    private val skyBox: SkyBox
    private val camera: PerspectiveCamera
    private val panorama_overlay: Texture?
    private val layout: BaseLayout
    private val layoutTexts: BaseLayout
    private val resSplashes: Resource
    private val splashTextView: TextView

    init {

        // Panorama
        skyBox = SkyBox(
            "vanilla/textures/gui/title/background/panorama_1.png",
            "vanilla/textures/gui/title/background/panorama_3.png",
            "vanilla/textures/gui/title/background/panorama_4.png",
            "vanilla/textures/gui/title/background/panorama_5.png",
            "vanilla/textures/gui/title/background/panorama_0.png",
            "vanilla/textures/gui/title/background/panorama_2.png"
        )
        camera = PerspectiveCamera(0.1, 2.0, 79.0)
        camera.rotation[90f] = -25f
        panorama_overlay = session.resourceManager.getTexture("panorama_overlay").texture

        // Main Layout
        layout = BaseLayout()
        layout.setLayoutType(LayoutType.VERTICAL)
        layout.alignItems(Align.UP)

        // <----------TITLE---------->
        // [   Mine|craft   ]
        // [  Java Edition  ]
        //         < SPLASH >

        // Title (MINECRAFT)
        val titleLeftPartTexture = session.resourceManager.getTexture("title_left_part")
        val titleRightPartTexture = session.resourceManager.getTexture("title_right_part")
        val titleEditionTexture = session.resourceManager.getTexture("title_edition")
        val titleLayout = Layout()
        titleLayout.setLayoutType(LayoutType.HORIZONTAL)
        titleLayout.setY(GapConstraint.gap(25.0))
        titleLayout.setSize(
            aspect((titleLeftPartTexture!!.aspect() + titleRightPartTexture!!.aspect()).toDouble()),
            pixel(45.0)
        )
        layout.put("title", titleLayout)
        val titleLeftPart = Image(titleLeftPartTexture)
        titleLeftPart.setSize(aspect(titleLeftPartTexture.aspect().toDouble()), relative(1.0))
        titleLayout.put("title_left", titleLeftPart)
        val titleRightPart = Image(titleRightPartTexture)
        titleRightPart.setSize(aspect(titleRightPartTexture.aspect().toDouble()), relative(1.0))
        titleLayout.put("title_right", titleRightPart)

        // Edition (Java Edition)
        val edition = Image(titleEditionTexture)
        edition.setSize(aspect(7.0), pixel(13.0))
        edition.setY(GapConstraint.gap(-7.0))
        layout.put("edition", edition)

        // Splash
        resSplashes = Resource("vanilla/texts/splashes.txt")
        splashTextView =
            TextView(session, Component().color(SPLASH_COLOR).style(StyleFormatting.ITALIC).formattedText("Splash!"))
        splashTextView.setSize(pixel(10.0))
        splashTextView.rotation = 15f
        layout.put("splash", splashTextView)

        // <----------BUTTONS: 1-2 LINE---------->
        // [ Singleplayer ]
        // [  Multiplayer ]
        // [     Mods     ]

        // Singleplayer
        val singleplayerButton = Button(session, Component().translation("menu.singleplayer"))
        singleplayerButton.setClickListener { toScreen("world_selection") }
        singleplayerButton.setY(GapConstraint.gap(45.0))
        singleplayerButton.setSize(pixel(200.0), pixel(20.0)) //////////
        layout.put("singleplayer", singleplayerButton)

        // Multiplayer
        val multiplayerButton = Button(session, Component().translation("menu.multiplayer"))
        multiplayerButton.setClickListener { println("Multiplayer") }
        multiplayerButton.setY(GapConstraint.gap(4.0))
        multiplayerButton.setSize(pixel(200.0), pixel(20.0)) ////////////
        layout.put("multiplayer", multiplayerButton)

        // Mods
        val modsButton = Button(session, Component().translation("menu.mods"))
        modsButton.setClickListener { println("Mods") }
        modsButton.setY(GapConstraint.gap(4.0))
        modsButton.setSize(pixel(200.0), pixel(20.0)) /////////////
        layout.put("mods", modsButton)

        // <----------BUTTONS: 3 LINE---------->
        // [ Options... ] [ Quit Game ]

        // Horizontal Layout
        val horizontalLayout = Layout()
        horizontalLayout.setY(GapConstraint.gap(15.0))
        horizontalLayout.setSize(pixel(200.0), pixel(20.0)) ///////////
        horizontalLayout.setLayoutType(LayoutType.HORIZONTAL)
        layout.put("horizontal_layout", horizontalLayout)

        // Options...
        val optionsButton = Button(session, Component().translation("menu.options"))
        optionsButton.setClickListener { toScreen("options") }
        optionsButton.setSize(pixel(97.0), relative(1.0))
        horizontalLayout.put("options", optionsButton)

        // Quit Game
        val quitButton = Button(session, Component().translation("menu.quit"))
        quitButton.setClickListener { close() }
        quitButton.alignSelf(Align.RIGHT)
        quitButton.setSize(pixel(97.0), relative(1.0))
        horizontalLayout.put("quit", quitButton)

        // <----------TEXTS---------->
        // < Version > < Copyright >

        // Texts Layout
        layoutTexts = BaseLayout()

        // Version (Minecraft 1.0.0)
        val versionTextView = TextView(session, Component().formattedText("Minecraft §n1§r.§n01§r.§n0"))
        versionTextView.setPosition(pixel(2.0))
        layoutTexts.put("version", versionTextView)

        // Copyright (Pashok AB)
        val copyrightTextView =
            TextView(session, Component().formattedText("Copyright §oПашок§r AB. Do not distribute!"))
        copyrightTextView.setPosition(pixel(1.0))
        copyrightTextView.alignSelf(Align.RIGHT_DOWN)
        layoutTexts.put("copyright", copyrightTextView)
    }

    override fun render(batch: TextureBatch) {
        // Panorama
        camera.rotation.yaw -= dt * 2
        camera.update()
        skyBox.render(camera)
        // Panorama Overlay
        batch.setAlpha(0.8)
        batch.draw(panorama_overlay!!, 0f, 0f, width.toFloat(), height.toFloat())
        batch.setAlpha(1.0)

        // UI
        layout.render(batch)
        layoutTexts.render(batch)

        // Splash
        // ..Code
    }

    override fun onShow() {
        camera.rotation[90f] = -25f
    }

    override fun resize(width: Int, height: Int) {}
    override fun close() {
        exit()
    }

    override fun dispose() {
        skyBox.dispose()
    }

    override fun shouldCloseOnEsc(): Boolean {
        return false
    }

    override fun renderDirtBackground(): Boolean {
        return false
    }

    companion object {
        val SPLASH_COLOR = ImmutableColor(1f, 1f, 0f, 1f)
    }
}
