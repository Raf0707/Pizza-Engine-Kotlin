package pize.tests.net

import pize.net.security.KeyAES
import pize.net.security.KeyRSA
import pize.net.tcp.TcpConnection
import pize.net.tcp.TcpListener
import pize.net.tcp.TcpServer
import pize.net.tcp.packet.Packets.getPacketInfo
import pize.tests.net.packet.EncodePacket
import pize.tests.net.packet.MessagePacket
import pize.tests.net.packet.PingPacket

class ServerSide : TcpListener {
    private val server: TcpServer
    private val key: KeyRSA

    init {
        key = KeyRSA(2048)
        server = TcpServer(this)
        server.run("localhost", 5454)
        while (!Thread.interrupted());
        server.close()
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
                    println("   client->server ping: " + (System.nanoTime() - packet.time) / 1000000f)
                    packet.write(sender)
                }

                EncodePacket.Companion.PACKET_TYPE_ID -> {
                    val packet = packetInfo.readPacket(EncodePacket())
                    val clientKey = KeyAES(key.decrypt(packet.key))
                    println("   client's key received")
                    sender!!.encode(clientKey)
                    println("   encoded with key (hash): " + clientKey.key.hashCode())
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun connected(connection: TcpConnection?) {
        println("Client connected {")
        EncodePacket(key.public!!.key!!.encoded).write(connection)
        println("   send public key")
    }

    override fun disconnected(connection: TcpConnection?) {
        println("   Client disconnected\n}")
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            ServerSide()
        }
    }
}
