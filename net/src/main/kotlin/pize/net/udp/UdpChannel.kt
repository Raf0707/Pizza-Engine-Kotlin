package pize.net.udp

import pize.net.tcp.packet.PacketHandler
import pize.net.tcp.TcpConnection
import pize.net.tcp.PacketWriter
import pize.util.io.PizeOutputStream
import java.io.IOException
import pize.util.io.PizeInputStream
import pize.net.tcp.packet.PacketInfo
import java.io.ByteArrayInputStream
import java.lang.RuntimeException
import pize.net.tcp.packet.IPacket
import pize.net.tcp.TcpListener
import pize.net.tcp.TcpDisconnector
import java.net.InetSocketAddress
import java.net.InetAddress
import java.io.ByteArrayOutputStream
import java.io.Closeable
import java.net.ServerSocket
import java.util.concurrent.CopyOnWriteArrayList
import java.lang.Runnable
import java.io.DataOutputStream
import pize.net.security.KeyAES
import java.net.SocketException
import java.io.DataInputStream
import pize.net.udp.UdpListener
import pize.net.udp.UdpChannel
import pize.net.udp.UdpClient
import java.net.DatagramSocket
import java.net.SocketAddress
import java.net.DatagramPacket
import pize.net.udp.UdpServer
import org.lwjgl.BufferUtils
import javax.crypto.SecretKey
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import javax.crypto.KeyGenerator
import javax.crypto.IllegalBlockSizeException
import javax.crypto.BadPaddingException
import pize.net.security.PublicRSA
import pize.net.security.PrivateRSA
import java.nio.ByteBuffer
import java.security.KeyPairGenerator
import java.security.NoSuchAlgorithmException
import java.security.PublicKey
import java.security.KeyFactory
import java.security.spec.X509EncodedKeySpec
import java.security.PrivateKey
import java.security.spec.PKCS8EncodedKeySpec

class UdpChannel(private val socket: DatagramSocket, listener: UdpListener) {
    private val thread: Thread
    var isClosed = false
        private set

    fun send(packet: DatagramPacket) {
        if (isClosed) return
        try {
            val length = BufferUtils.createByteBuffer(4).putInt(packet.length).array()
            val sizePacket = DatagramPacket(length, 4, packet.socketAddress)
            socket.send(sizePacket)
            socket.send(packet)
        } catch (ignored: IOException) {
        }
    }

    fun close() {
        thread.interrupt()
        socket.close()
        isClosed = true
    }

    init {
        thread = Thread {
            try {
                while (!Thread.interrupted()) {
                    val sizePacket = DatagramPacket(ByteArray(4), 4)
                    socket.receive(sizePacket)
                    val length = ByteBuffer.wrap(sizePacket.data).int
                    val packet = DatagramPacket(ByteArray(length), length)
                    socket.receive(packet)
                    listener.received(packet)
                    Thread.yield()
                }
            } catch (ignored: Exception) {
            }
        }
        thread.priority = Thread.MIN_PRIORITY
        thread.isDaemon = true
        thread.start()
    }
}