package pize.tests.minecraft.game.gui.screen.screens

import pize.graphics.util.batch.TextureBatch
import pize.gui.Align
import pize.gui.LayoutType
import pize.gui.constraint.Constraint.Companion.aspect
import pize.gui.constraint.Constraint.Companion.pixel
import pize.gui.constraint.Constraint.Companion.relative
import pize.math.Maths.round
import pize.tests.minecraft.game.Session
import pize.tests.minecraft.game.gui.components.*
import pize.tests.minecraft.game.gui.constraints.GapConstraint
import pize.tests.minecraft.game.gui.text.Component

class VideoSettingsScreen(session: Session) : IOptionsScreen(session) {
    private val layout: BaseLayout
    private val fovSlider: Slider
    private val renderDistanceSlider: Slider
    private val maxFramerateSlider: Slider
    private val mipmapSlider: Slider

    init {

        // Main Layout
        layout = BaseLayout()
        layout.setLayoutType(LayoutType.VERTICAL)
        layout.alignItems(Align.UP)

        // <----------TEXTS---------->
        // < Title >

        // Title
        val title = TextView(session, Component().translation("videoSettings.title"))
        title.setY(GapConstraint.gap(108.0))
        layout.put("title", title)

        // <----------OPTIONS: 1 LINE---------->
        // [ FOV ] [ Render Distance ]

        // 1 Line Layout
        val layoutLine1 = Layout()
        layoutLine1.setY(GapConstraint.gap(15.0))
        layoutLine1.setSize(aspect(16.0), pixel(20.0))
        layout.put("1lineLayout", layoutLine1)
        // Fov
        fovSlider = Slider(session)
        fovSlider.setSize(pixel(155.0), relative(1.0))
        fovSlider.text = Component()
            .translation("videoSettings.fov", Component().text(options.fov.toString()))
        fovSlider.setValue((options.fov / (MAX_FOV - MIN_FOV)).toDouble())
        layoutLine1.put("fov", fovSlider)
        // Render Distance
        renderDistanceSlider = Slider(session)
        renderDistanceSlider.setSize(pixel(155.0), relative(1.0))
        renderDistanceSlider.text = Component().translation(
            "videoSettings.renderDistance",
            Component().text(options.renderDistance.toString())
        )
        renderDistanceSlider.setValue((options.renderDistance / (MAX_RENDER_DISTANCE - MIN_RENDER_DISTANCE)).toDouble())
        renderDistanceSlider.alignSelf(Align.RIGHT)
        layoutLine1.put("renderDistance", renderDistanceSlider)
        // <----------OPTIONS: 2 LINE---------->
        // [ Max Framerate ] [ Show FPS ]

        // 2 Line Layout
        val layoutLine2 = Layout()
        layoutLine2.setY(GapConstraint.gap(4.0))
        layoutLine2.setSize(aspect(16.0), pixel(20.0))
        layout.put("2lineLayout", layoutLine2)
        // Max Framerate
        maxFramerateSlider = Slider(session)
        maxFramerateSlider.setSize(pixel(155.0), relative(1.0))
        maxFramerateSlider.text = Component().translation(
            "videoSettings.maxFramerate",
            setMaxFramerateComponent(Component(), options.maxFramerate)
        )
        maxFramerateSlider.setValue((options.maxFramerate.toFloat() / MAX_SETTING_FRAMERATE).toDouble())
        maxFramerateSlider.setDivisions(MAX_SETTING_FRAMERATE / FRAMERATE_SETTING_INTERVAL)
        layoutLine2.put("maxFramerate", maxFramerateSlider)
        // Show FPS
        val showFpsButton = Button(
            session, Component().translation(
                "videoSettings.showFps", setBooleanComponent(
                    Component(), options.isShowFps
                )
            )
        )
        showFpsButton.setSize(pixel(155.0), relative(1.0))
        showFpsButton.alignSelf(Align.RIGHT)
        layoutLine2.put("showFPS", showFpsButton)
        // <----------OPTIONS: 3 LINE---------->
        // [ Mipmap Levels ] [ Fullscreen ]

        // 3 Line Layout
        val layoutLine3 = Layout()
        layoutLine3.setY(GapConstraint.gap(4.0))
        layoutLine3.setSize(aspect(16.0), pixel(20.0))
        layout.put("3lineLayout", layoutLine3)
        // Mipmap Levels
        mipmapSlider = Slider(session)
        mipmapSlider.setSize(pixel(155.0), relative(1.0))
        mipmapSlider.text = Component().translation(
            "videoSettings.mipmapLevels",
            Component().text(options.mipmapLevels.toString())
        )
        mipmapSlider.setValue((options.mipmapLevels.toFloat() / MAX_MIPMAP_LEVELS).toDouble())
        mipmapSlider.setDivisions(MAX_MIPMAP_LEVELS)
        layoutLine3.put("mipmap", mipmapSlider)
        // Fullscreen
        val fullscreenButton = Button(
            session, Component().translation(
                "videoSettings.fullscreen", setBooleanComponent(
                    Component(), options.isFullscreen
                )
            )
        )
        fullscreenButton.setSize(pixel(155.0), relative(1.0))
        fullscreenButton.alignSelf(Align.RIGHT)
        layoutLine3.put("fullscreen", fullscreenButton)
        // <----------DONE---------->
        // [ Done ]
        val doneButton = Button(session, Component().translation("gui.done"))
        doneButton.setClickListener { close() }
        doneButton.setY(GapConstraint.gap(15.0))
        doneButton.setSize(aspect(10.0), pixel(20.0))
        layout.put("done", doneButton)
        // Done

        // <----------CALLBACKS---------->
        showFpsButton.setClickListener {
            options.setShowFps(!options.isShowFps)
            setBooleanComponent(
                showFpsButton.text.getComponentAsTranslation(0).getArg(0).clear().reset(),
                options.isShowFps
            )
        }
        fullscreenButton.setClickListener {
            options.setFullscreen(!options.isFullscreen)
            setBooleanComponent(
                fullscreenButton.text.getComponentAsTranslation(0).getArg(0).clear().reset(),
                options.isFullscreen
            )
        }
    }

    override fun render(batch: TextureBatch) {
        layout.render(batch)
        if (fovSlider.isChanged) {
            options.fov =
                round((fovSlider.value * (MAX_FOV - MIN_FOV) + MIN_FOV).toDouble())
            fovSlider.text.getComponentAsTranslation(0).getArg(0).clear().text(session.options.fov)
        }
        if (renderDistanceSlider.isChanged) {
            options.renderDistance =
                round((renderDistanceSlider.value * (MAX_RENDER_DISTANCE - MIN_RENDER_DISTANCE) + MIN_RENDER_DISTANCE).toDouble())
            renderDistanceSlider.text.getComponentAsTranslation(0).getArg(0).clear()
                .text(session.options.renderDistance.toString())
        }
        if (maxFramerateSlider.isChanged) {
            val maxFramerate = round((maxFramerateSlider.value * MAX_SETTING_FRAMERATE).toDouble())
            setMaxFramerateComponent(
                maxFramerateSlider.text.getComponentAsTranslation(0).getArg(0).clear().reset(), maxFramerate
            )
        }
        if (maxFramerateSlider.isTouchReleased) {
            options.setMaxFramerate(
                round((maxFramerateSlider.value * MAX_SETTING_FRAMERATE).toDouble()),
                MAX_SETTING_FRAMERATE
            )
            saveOptions()
        }
        if (mipmapSlider.isChanged) {
            val mipmapLevels = round((mipmapSlider.value * MAX_MIPMAP_LEVELS).toDouble())
            mipmapSlider.text.getComponentAsTranslation(0).getArg(0).clear().text(mipmapLevels.toString())
        }
        if (mipmapSlider.isTouchReleased) {
            options.mipmapLevels =
                round((mipmapSlider.value * MAX_MIPMAP_LEVELS).toDouble())
            saveOptions()
        }
    }

    private fun setMaxFramerateComponent(component: Component?, maxFramerate: Int): Component? {
        if (maxFramerate == 0) component!!.translation("text.vSync") else if (maxFramerate == MAX_SETTING_FRAMERATE) component!!.translation(
            "text.unlimited"
        ) else component!!.text(maxFramerate.toString())
        return component
    }

    private fun setBooleanComponent(component: Component, showFPS: Boolean): Component {
        if (showFPS) component.translation("text.on") else component.translation("text.off")
        return component
    }

    override fun resize(width: Int, height: Int) {}
    override fun onShow() {}
    override fun close() {
        toScreen("options")
    }

    companion object {
        const val MIN_FOV = 30f
        const val MAX_FOV = 110f
        const val MIN_RENDER_DISTANCE = 1f
        const val MAX_RENDER_DISTANCE = 128f
        const val MAX_SETTING_FRAMERATE = 255
        const val FRAMERATE_SETTING_INTERVAL = 5
        const val MAX_MIPMAP_LEVELS = 4
    }
}