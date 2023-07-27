package pize.tests.minecraft.game.gui.screen.screens

import pize.audio.Audio.Companion.availableDevices
import pize.graphics.util.batch.TextureBatch
import pize.gui.Align
import pize.gui.LayoutType
import pize.gui.constraint.Constraint.Companion.aspect
import pize.gui.constraint.Constraint.Companion.pixel
import pize.gui.constraint.Constraint.Companion.relative
import pize.tests.minecraft.game.Session
import pize.tests.minecraft.game.gui.components.*
import pize.tests.minecraft.game.gui.constraints.GapConstraint
import pize.tests.minecraft.game.gui.text.Component
import pize.tests.minecraft.game.options.SoundCategory

class AudioSettingsScreen(session: Session) : IOptionsScreen(session) {
    private val layout: BaseLayout
    private val masterVolume: VolumeSlider
    private val musicVolume: VolumeSlider
    private val ambientVolume: VolumeSlider
    private val playersVolume: VolumeSlider
    private val blocksVolume: VolumeSlider
    private val weatherVolume: VolumeSlider

    init {

        // Main Layout
        layout = BaseLayout()
        layout.setLayoutType(LayoutType.VERTICAL)
        layout.alignItems(Align.UP)

        // <----------TEXTS---------->
        // < Title >
        val titleTextView = TextView(session, Component().translation("audioSettings.title"))
        titleTextView.setY(GapConstraint.gap(71.0))
        layout.put("title", titleTextView)
        // Title
        val list = ListView()
        list.setLayoutType(LayoutType.VERTICAL)
        list.alignItems(Align.UP)
        list.setSize(relative(0.8), relative(0.3))
        list.setY(GapConstraint.gap(3.0))
        layout.put("list", list)


        // <----------LINE 1---------->
        // [ Master Volume ]
        masterVolume = VolumeSlider(session, SoundCategory.MASTER)
        masterVolume.setY(GapConstraint.gap(15.0))
        masterVolume.setSize(aspect(16.0), pixel(20.0))
        list.put("master", masterVolume)
        // Master Volume

        // <----------LINE 2---------->
        // [ Music ] [ Ambient ]

        // 2 Line Layout
        val layoutLine2 = Layout()
        layoutLine2.setLayoutType(LayoutType.HORIZONTAL)
        layoutLine2.setY(GapConstraint.gap(4.0))
        layoutLine2.setSize(aspect(16.0), pixel(20.0))
        list.put("line2layout", layoutLine2)
        // Music
        musicVolume = VolumeSlider(session, SoundCategory.MUSIC)
        musicVolume.setSize(pixel(155.0), relative(1.0))
        layoutLine2.put("music", musicVolume)
        // Ambient
        ambientVolume = VolumeSlider(session, SoundCategory.AMBIENT)
        ambientVolume.alignSelf(Align.RIGHT)
        ambientVolume.setSize(pixel(155.0), relative(1.0))
        layoutLine2.put("ambient", ambientVolume)
        // <----------LINE 3---------->
        // [ Players ] [ Blocks ]

        // 3 Line Layout
        val layoutLine3 = Layout()
        layoutLine3.setLayoutType(LayoutType.HORIZONTAL)
        layoutLine3.setY(GapConstraint.gap(4.0))
        layoutLine3.setSize(aspect(16.0), pixel(20.0))
        list.put("line3layout", layoutLine3)
        // Players
        playersVolume = VolumeSlider(session, SoundCategory.PLAYERS)
        playersVolume.setSize(pixel(155.0), relative(1.0))
        layoutLine3.put("players", playersVolume)
        // Blocks
        blocksVolume = VolumeSlider(session, SoundCategory.BLOCKS)
        blocksVolume.alignSelf(Align.RIGHT)
        blocksVolume.setSize(pixel(155.0), relative(1.0))
        layoutLine3.put("blocks", blocksVolume)
        // <----------LINE 4---------->
        // [ Wather ]

        // 4 Line Layout
        val layoutLine4 = Layout()
        layoutLine4.setLayoutType(LayoutType.HORIZONTAL)
        layoutLine4.setY(GapConstraint.gap(4.0))
        layoutLine4.setSize(aspect(16.0), pixel(20.0))
        list.put("line4layout", layoutLine4)
        // Weather
        weatherVolume = VolumeSlider(session, SoundCategory.WEATHER)
        weatherVolume.setSize(pixel(155.0), relative(1.0))
        layoutLine4.put("weather", weatherVolume)
        // <----------LINE 5---------->
        // [ Device ]

        // Device
        val deviceButton = Button(
            session,
            Component().translation("audioSettings.device", Component().formattedText(session.options.audioDevice))
        )
        deviceButton.setY(GapConstraint.gap(4.0))
        deviceButton.setSize(aspect(16.0), pixel(20.0))
        list.put("device", deviceButton)

        // <----------DONE---------->
        // [ Done ]

        // Done
        val doneButton = Button(session, Component().translation("gui.done"))
        doneButton.setY(GapConstraint.gap(4.0))
        doneButton.setSize(aspect(10.0), pixel(20.0))
        doneButton.setClickListener { close() }
        layout.put("done", doneButton)

        // <----------CALLBACKS---------->

        // Device
        deviceButton.setClickListener(object : Runnable {
            var deviceIndex = 0
            override fun run() {
                val list = availableDevices
                if (list != null) {
                    deviceIndex++
                    if (deviceIndex >= list.size) deviceIndex = 0
                    val nextDevice = list[deviceIndex]
                    deviceButton.text =
                        Component().translation("audioSettings.device", Component().formattedText(nextDevice))
                    session.options.setAudioDevice(nextDevice)
                    session.options.save()
                }
            }
        })
    }

    override fun render(batch: TextureBatch) {
        masterVolume.updateVolume()
        musicVolume.updateVolume()
        ambientVolume.updateVolume()
        playersVolume.updateVolume()
        blocksVolume.updateVolume()
        weatherVolume.updateVolume()
        layout.render(batch)
    }

    override fun resize(width: Int, height: Int) {}
    override fun onShow() {}
    override fun close() {
        toScreen("options")
    }
}