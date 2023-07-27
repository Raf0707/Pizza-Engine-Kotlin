package pize.tests.voxelgame.main.net.packet

import pize.math.vecmath.vector.Vec3f
import pize.net.tcp.packet.IPacket
import pize.net.tcp.packet.PacketHandler
import pize.tests.voxelgame.main.audio.Sound
import pize.util.io.PizeInputStream
import pize.util.io.PizeOutputStream
import java.io.IOException

class CBPacketPlaySound() : IPacket<PacketHandler?>(PACKET_ID) {
    var soundID: String?
    var volume: Float
    var pitch: Float
    var x: Float
    var y: Float
    var z: Float

    constructor(soundID: String?, volume: Float, pitch: Float, x: Float, y: Float, z: Float) : this() {
        this.soundID = soundID
        this.volume = volume
        this.pitch = pitch
        this.x = x
        this.y = y
        this.z = z
    }

    constructor(sound: Sound?, volume: Float, pitch: Float, x: Float, y: Float, z: Float) : this(
        sound.getID(),
        volume,
        pitch,
        x,
        y,
        z
    )

    constructor(sound: Sound, volume: Float, pitch: Float, position: Vec3f) : this(
        sound.id,
        volume,
        pitch,
        position.x,
        position.y,
        position.z
    )

    @Throws(IOException::class)
    override fun write(stream: PizeOutputStream?) {
        stream!!.writeUTF(soundID)
        stream.writeFloat(volume)
        stream.writeFloat(pitch)
        stream.writeFloat(x)
        stream.writeFloat(y)
        stream.writeFloat(z)
    }

    @Throws(IOException::class)
    override fun read(stream: PizeInputStream?) {
        soundID = stream!!.readUTF()
        volume = stream.readFloat()
        pitch = stream.readFloat()
        x = stream.readFloat()
        y = stream.readFloat()
        z = stream.readFloat()
    }

    companion object {
        const val PACKET_ID = 23
    }
}
