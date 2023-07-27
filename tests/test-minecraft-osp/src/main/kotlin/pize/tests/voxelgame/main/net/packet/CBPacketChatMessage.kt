package pize.tests.voxelgame.main.net.packet

import pize.net.tcp.packet.IPacket
import pize.net.tcp.packet.PacketHandler
import pize.tests.voxelgame.main.chat.MessageSource
import pize.tests.voxelgame.main.chat.MessageSourcePlayer
import pize.tests.voxelgame.main.chat.MessageSourceServer
import pize.tests.voxelgame.main.chat.MessageSources
import pize.tests.voxelgame.main.text.ComponentText
import pize.tests.voxelgame.main.text.TextStyle
import pize.util.io.PizeInputStream
import pize.util.io.PizeOutputStream
import java.io.IOException

class CBPacketChatMessage() : IPacket<PacketHandler?>(PACKET_ID) {
    var source: MessageSource
    var playerName: String? = null
    var components: MutableList<ComponentText?>?

    private constructor(source: MessageSource, components: MutableList<ComponentText?>?) : this() {
        this.source = source
        this.components = components
    }

    constructor(components: MutableList<ComponentText?>?) : this(MessageSourceServer(), components)
    constructor(playerName: String?, components: MutableList<ComponentText?>?) : this(
        MessageSourcePlayer(playerName),
        components
    ) {
        this.playerName = playerName
    }

    @Throws(IOException::class)
    override fun write(stream: PizeOutputStream?) {
        // Write source
        stream!!.writeByte(source.source.ordinal)
        if (source.source == MessageSources.PLAYER) stream.writeUTF(playerName)

        // Write components
        stream.writeShort(components!!.size)
        for (component in components!!) writeComponent(stream, component)
    }

    @Throws(IOException::class)
    private fun writeComponent(stream: PizeOutputStream?, component: ComponentText?) {
        stream!!.writeByte(component.getStyle().data.toInt())
        stream.writeColor(component.getColor())
        stream.writeUTF(component.getText())
    }

    @Throws(IOException::class)
    override fun read(stream: PizeInputStream?) {
        // Read source
        val messageSource = MessageSources.entries[stream!!.readByte().toInt()]
        if (messageSource == MessageSources.PLAYER) {
            playerName = stream.readUTF()
            source = MessageSourcePlayer(playerName)
        } else source = MessageSourceServer()

        // Read components
        components = ArrayList()
        val componentsNum = stream.readShort()
        for (i in 0 until componentsNum) readComponent(stream)
    }

    @Throws(IOException::class)
    private fun readComponent(stream: PizeInputStream?) {
        components!!.add(
            ComponentText(
                TextStyle(stream!!.readByte()),
                stream.readColor(),
                stream.readUTF()
            )
        )
    }

    companion object {
        const val PACKET_ID = 18
    }
}