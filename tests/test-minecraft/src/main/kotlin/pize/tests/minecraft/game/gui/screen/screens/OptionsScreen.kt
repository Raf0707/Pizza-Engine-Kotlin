package pize.tests.minecraft.game.gui.screen.screens

import pize.graphics.util.batch.TextureBatch
import pize.gui.Align
import pize.gui.LayoutType
import pize.gui.constraint.Constraint.Companion.aspect
import pize.gui.constraint.Constraint.Companion.pixel
import pize.gui.constraint.Constraint.Companion.relative
import pize.tests.minecraft.game.Session
import pize.tests.minecraft.game.gui.components.BaseLayout
import pize.tests.minecraft.game.gui.components.Button
import pize.tests.minecraft.game.gui.components.Layout
import pize.tests.minecraft.game.gui.components.TextView
import pize.tests.minecraft.game.gui.constraints.GapConstraint
import pize.tests.minecraft.game.gui.text.Component

class OptionsScreen(session: Session) : IOptionsScreen(session) {
    private val layout: BaseLayout

    init {

        // [Main Layout]
        layout = BaseLayout()
        layout.setLayoutType(LayoutType.VERTICAL)
        layout.alignItems(Align.UP)

        // <----------TEXTS---------->
        // < Title >

        // Title (Options)
        val titleTextView = TextView(session, Component().translation("options.title"))
        titleTextView.setY(GapConstraint.gap(108.0))
        layout.put("title", titleTextView)

        // <----------LINE 1---------->
        // [ Video Settings ] [ Music & Sounds ]

        // 1 Line Layout
        val layoutLine1 = Layout()
        layoutLine1.setY(GapConstraint.gap(15.0))
        layoutLine1.setSize(aspect(16.0), pixel(20.0))
        layoutLine1.setLayoutType(LayoutType.HORIZONTAL)
        layout.put("layout_line_1", layoutLine1)

        // Video Settings
        val videoSettingsButton = Button(session, Component().translation("options.videoSettings"))
        videoSettingsButton.setClickListener { toScreen("video_settings") }
        videoSettingsButton.setSize(pixel(155.0), relative(1.0))
        layoutLine1.put("video_settings", videoSettingsButton)

        // Music & Sounds
        val audioSettingsButton = Button(session, Component().translation("options.audioSettings"))
        audioSettingsButton.setClickListener { toScreen("audio_settings") }
        audioSettingsButton.setSize(pixel(155.0), relative(1.0))
        audioSettingsButton.alignSelf(Align.RIGHT)
        layoutLine1.put("audio_settings", audioSettingsButton)

        // <----------LINE 2---------->
        // [ Language ] [ Controls ]

        // 2 Line Layout
        val layoutLine2 = Layout()
        layoutLine2.setY(GapConstraint.gap(4.0))
        layoutLine2.setSize(aspect(16.0), pixel(20.0))
        layoutLine2.setLayoutType(LayoutType.HORIZONTAL)
        layout.put("layout_line_2", layoutLine2)

        // Language
        val languageButton = Button(session, Component().translation("options.language"))
        languageButton.setSize(pixel(155.0), relative(1.0))
        layoutLine2.put("language", languageButton)

        // Controls
        val controlsButton = Button(session, Component().translation("options.controls"))
        controlsButton.setClickListener { println("Controls") }
        controlsButton.setSize(pixel(155.0), relative(1.0))
        controlsButton.alignSelf(Align.RIGHT)
        layoutLine2.put("controls", controlsButton)

        // <----------LINE 3---------->
        // [ Resource Packs ]

        // 3 Line Layout
        val layoutLine3 = Layout()
        layoutLine3.setY(GapConstraint.gap(4.0))
        layoutLine3.setSize(aspect(16.0), pixel(20.0))
        layoutLine3.setLayoutType(LayoutType.HORIZONTAL)
        layout.put("layout_line_3", layoutLine3)

        // Resource Packs
        val resourcePacksButton = Button(session, Component().translation("options.resourcePacks"))
        resourcePacksButton.setClickListener { println("Resource Packs") }
        resourcePacksButton.setSize(pixel(155.0), relative(1.0))
        layoutLine3.put("resource_packs", resourcePacksButton)

        // <----------DONE---------->
        // [ Done ]

        // Done
        val doneButton = Button(session, Component().translation("gui.done"))
        doneButton.setClickListener { close() }
        doneButton.setY(GapConstraint.gap(15.0))
        doneButton.setSize(aspect(10.0), pixel(20.0))
        layout.put("done", doneButton)

        // <----------CALLBACKS---------->

        // Language
        languageButton.setClickListener(object : Runnable {
            var langIndex = 0
            override fun run() {
                val availableLanguages = session.languageManager.availableLanguages
                if (availableLanguages!!.size != 0) {
                    langIndex++
                    if (langIndex >= availableLanguages.size) langIndex = 0
                    val languageCode = availableLanguages[langIndex].code
                    println("Selected Lang: $languageCode")
                    session.languageManager.selectLanguage(languageCode)
                    session.options.language = languageCode
                    session.options.save()
                }
            }
        })
    }

    override fun render(batch: TextureBatch) {
        layout.render(batch)
    }

    override fun resize(width: Int, height: Int) {}
    override fun onShow() {}
    override fun close() {
        toScreen("main_menu")
        session.options.save()
    }
}