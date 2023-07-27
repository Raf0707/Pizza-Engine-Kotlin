package pize.tests.net

import pize.Pize.create
import pize.Pize.exit
import pize.Pize.run
import pize.app.AppAdapter
import pize.graphics.font.BitmapFont
import pize.graphics.font.FontLoader.loadFnt
import pize.graphics.gl.Gl.clearColor
import pize.graphics.gl.Gl.clearColorBuffer
import pize.graphics.util.batch.TextureBatch
import pize.io.glfw.Key
import pize.net.security.KeyAES
import pize.net.security.PublicRSA
import pize.net.tcp.TcpClient
import pize.net.tcp.TcpConnection
import pize.net.tcp.TcpListener
import pize.net.tcp.packet.Packets.getPacketInfo
import pize.tests.net.packet.EncodePacket
import pize.tests.net.packet.MessagePacket
import pize.tests.net.packet.PingPacket
import pize.util.io.TextProcessor
import java.util.*

class ClientSide : AppAdapter(), TcpListener {
    private var client: TcpClient? = null
    private var key: KeyAES? = null
    private var batch: TextureBatch? = null
    private var font: BitmapFont? = null
    private var text: TextProcessor? = null
    override fun init() {
        batch = TextureBatch()
        font = loadFnt("default.fnt")
        font!!.scale = 3f
        text = TextProcessor(false)
        key = KeyAES(256)
        client = TcpClient(this)
        client!!.connect("localhost", 5454)
        client!!.connection!!.setTcpNoDelay(true)
    }

    override fun render() {
        if (Key.ENTER.isDown) {
            MessagePacket(text.toString()).write(client!!.connection)
            text!!.removeLine()
        }
        if (Key.LEFT_CONTROL.isPressed && Key.P.isDown) PingPacket(System.nanoTime()).write(
            client!!.connection
        )
        if (Key.ESCAPE.isDown) exit()
        clearColorBuffer()
        clearColor(0.2, 0.2, 0.2, 1.0)
        batch!!.begin()

        // Draw background
        val bounds = font!!.getBounds(text.toString())
        batch!!.drawQuad(0.1, 0.15, 0.2, 1.0, 50f, 10f, bounds!!.x, bounds.y)
        batch!!.drawQuad(0.3, 0.45, 0.5, 1.0, 0f, 10f, 50f, bounds.y)

        // Draw line numbers
        val lineNumbersJoiner = StringJoiner("\n")
        for (i in text!!.getLines().indices) lineNumbersJoiner.add((i + 1).toString())
        font!!.drawText(batch!!, lineNumbersJoiner.toString(), 5f, 10f)

        // Draw text
        font!!.drawText(batch!!, text.toString(), 50f, 10f)

        // Draw cursor
        if (System.currentTimeMillis() / 500 % 2 == 0L && text!!.isActive) {
            val currentLine = text!!.currentLine
            val cursorY = font!!.getBounds(text.toString())!!.y - (text!!.cursorY + 1) * font!!.lineAdvanceScaled
            val cursorX = font!!.getLineWidth(currentLine!!.substring(0, text!!.cursorX))
            batch!!.drawQuad(1.0, 1.0, 1.0, 1.0, 50 + cursorX, 10 + cursorY, 2f, font!!.lineAdvanceScaled)
        }
        batch!!.end()
    }

    override fun dispose() {
        text!!.dispose()
        batch!!.dispose()
        font!!.dispose()
    }

    override fun received(bytes: ByteArray?, sender: TcpConnection?) {
        try {
            val packetInfo = getPacketInfo(bytes!!)
            if (packetInfo == null) {
                println("   received not packet")
                return
            }
            when (packetInfo.packetID) {
                MessagePacket.Companion.PACKET_TYPE_ID -> {
                    val packet = packetInfo.readPacket(MessagePacket())
                    println("   message: " + packet.message)
                }

                PingPacket.Companion.PACKET_TYPE_ID -> {
                    val packet = packetInfo.readPacket(PingPacket())
                    println("   ping: " + (System.nanoTime() - packet.time) / 1000000f + " ms")
                }

                EncodePacket.Companion.PACKET_TYPE_ID -> {
                    val packet = packetInfo.readPacket(EncodePacket())
                    val serverKey = PublicRSA(packet.key)
                    println("   server's public key received")
                    EncodePacket(serverKey.encrypt(key!!.key!!.encoded)).write(sender)
                    println("   send encrypted key")
                    sender!!.encode(key)
                    println("   encoded with key (hash): " + key!!.key.hashCode())
                    PingPacket(System.nanoTime()).write(client!!.connection)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun connected(connection: TcpConnection?) {
        println("Connected {")
    }

    override fun disconnected(connection: TcpConnection?) {
        println("   Client disconnected\n}")
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            create("Net - client", 1280, 720)
            run(ClientSide())
        }
    }
}
