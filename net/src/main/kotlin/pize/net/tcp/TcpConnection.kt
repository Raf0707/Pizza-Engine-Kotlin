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
import pize.util.Utils
import java.net.*
import java.security.KeyPairGenerator
import java.security.NoSuchAlgorithmException
import java.security.PublicKey
import java.security.KeyFactory
import java.security.spec.X509EncodedKeySpec
import java.security.PrivateKey
import java.security.spec.PKCS8EncodedKeySpec

class TcpConnection(val socket: Socket, listener: TcpListener, private val disconnector: TcpDisconnector) {
    private val outStream: DataOutputStream
    private val receiveThread: Thread
    var isClosed = false
        private set
    private var encodeKey: KeyAES? = null
    @kotlin.jvm.Synchronized
    fun send(bytes: ByteArray?) {
        var bytes = bytes
        if (isClosed) return
        try {
            if (encodeKey != null) bytes = encodeKey!!.encrypt(bytes)
            outStream.writeInt(bytes!!.size)
            outStream.write(bytes)
        } catch (ignored: IOException) {
        }
    }

    fun send(stream: ByteArrayOutputStream) {
        send(stream.toByteArray())
    }

    @kotlin.jvm.Synchronized
    fun send(data: PacketWriter) {
        val byteStream = ByteArrayOutputStream()
        val dataStream = PizeOutputStream(byteStream)
        data.write(dataStream)
        send(byteStream)
    }

    fun encode(encodeKey: KeyAES?) {
        this.encodeKey = encodeKey
    }

    fun setTcpNoDelay(on: Boolean) {
        try {
            socket.tcpNoDelay = on
        } catch (ignored: SocketException) {
        }
    }

    fun close() {
        if (isClosed) return
        setClosed()
        receiveThread.interrupt()
        Utils.close(socket)
    }

    private fun setClosed() {
        isClosed = true
        disconnector.disconnected(this)
    }

    init {
        outStream = DataOutputStream(socket.getOutputStream())
        receiveThread = Thread({
            try {
                DataInputStream(socket.getInputStream()).use { inStream ->
                    while (!Thread.interrupted() && !isClosed) {
                        val length: Int = inStream.readInt()
                        if (length < 0) continue
                        var bytes: ByteArray? = inStream.readNBytes(length)
                        if (encodeKey != null) {
                            bytes = encodeKey!!.decrypt(bytes)
                            if (bytes == null) continue
                        }
                        listener.received(bytes, this)
                    }
                }
            } catch (e: IOException) {
                setClosed()
            }
        }, "TcpConnection-Thread")
        receiveThread.priority = Thread.MIN_PRIORITY
        receiveThread.isDaemon = true
        receiveThread.start()
    }
}