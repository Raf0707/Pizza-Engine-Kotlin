package pize.audio

import org.lwjgl.openal.ALC
import org.lwjgl.openal.ALC10
import org.lwjgl.openal.ALC11
import org.lwjgl.openal.ALUtil
import pize.app.Disposable

class Audio : Disposable {
    val current: AudioDevice
    private val devices: MutableMap<String?, AudioDevice>

    init {
        devices = HashMap()
        current = AudioDevice(defaultOutputDevice)
        current.context.makeCurrent()
        devices[current.name] = current
    }

    fun getDevices(): Collection<AudioDevice> {
        return devices.values
    }

    fun getDevice(name: String?): AudioDevice? {
        if (!devices.containsKey(name)) {
            val availableDevices = availableDevices
            if (availableDevices != null && availableDevices.contains(name)) {
                val device = AudioDevice(defaultOutputDevice)
                devices[name] = device
            }
        }
        return devices[name]
    }

    override fun dispose() {
        ALC10.alcMakeContextCurrent(0)
        for (device in devices.values) device.dispose()
        ALC.destroy()
    }

    companion object {
        val defaultOutputDevice: String?
            get() = ALC10.alcGetString(0, ALC10.ALC_DEFAULT_DEVICE_SPECIFIER)
        val defaultInputDevice: String?
            get() = ALC10.alcGetString(0, ALC11.ALC_CAPTURE_DEFAULT_DEVICE_SPECIFIER)
        @JvmStatic
        val availableDevices: List<String?>?
            get() = if (ALC10.alcIsExtensionPresent(0, "ALC_ENUMERATION_EXT")) ALUtil.getStringList(
                0,
                ALC11.ALC_CAPTURE_DEVICE_SPECIFIER
            ) else null
    }
}
