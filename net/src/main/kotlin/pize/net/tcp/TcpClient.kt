package pize.net.tcp

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
import java.io.ByteArrayOutputStream
import java.io.Closeable
import java.util.concurrent.CopyOnWriteArrayList
import java.lang.Runnable
import java.io.DataOutputStream
import pize.net.security.KeyAES
import java.io.DataInputStream
import pize.net.udp.UdpListener
import pize.net.udp.UdpChannel
import pize.net.udp.UdpClient
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
import java.net.*
import java.security.KeyPairGenerator
import java.security.NoSuchAlgorithmException
import java.security.PublicKey
import java.security.KeyFactory
import java.security.spec.X509EncodedKeySpec
import java.security.PrivateKey
import java.security.spec.PKCS8EncodedKeySpec

class TcpClient(private val listener: TcpListener) : TcpDisconnector() {
    var connection: TcpConnection? = null
        private set

    fun connect(address: String?, port: Int) {
        if (connection != null && !connection!!.isClosed) throw RuntimeException("Already connected")
        try {
            val socket = Socket()
            val socketAddress = InetSocketAddress(InetAddress.getByName(address), port)
            socket.connect(socketAddress)
            connection = TcpConnection(socket, listener, this)
            listener.connected(connection)
        } catch (e: IOException) {
            System.err.println("TcpClient: " + e.message)
        }
    }

    fun send(packet: ByteArray?) {
        connection!!.send(packet)
    }

    fun send(stream: ByteArrayOutputStream) {
        connection!!.send(stream)
    }

    fun send(data: PacketWriter) {
        connection!!.send(data)
    }

    @kotlin.jvm.Synchronized
    fun disconnect() {
        if (connection == null || connection!!.isClosed) return
        connection!!.close()
        listener.disconnected(connection)
    }

    override fun disconnected(connection: TcpConnection) {
        listener.disconnected(connection)
    }
}