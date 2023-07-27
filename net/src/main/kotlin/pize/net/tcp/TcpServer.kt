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
import pize.util.Utils
import java.security.KeyPairGenerator
import java.security.NoSuchAlgorithmException
import java.security.PublicKey
import java.security.KeyFactory
import java.security.spec.X509EncodedKeySpec
import java.security.PrivateKey
import java.security.spec.PKCS8EncodedKeySpec

class TcpServer(private val listener: TcpListener) : TcpDisconnector(), Closeable {
    private var serverSocket: ServerSocket? = null
    private var connectionList: CopyOnWriteArrayList<TcpConnection>? = null
    var isClosed = true
        private set

    fun run(address: String?, port: Int) {
        if (!isClosed) throw RuntimeException("Server already running")
        try {
            serverSocket = ServerSocket()
            serverSocket!!.bind(InetSocketAddress(InetAddress.getByName(address), port))
            connectionList = CopyOnWriteArrayList()
            waitConnections()
            isClosed = false
        } catch (e: IOException) {
            System.err.println("TcpServer (run error): " + e.message)
        }
    }

    private fun waitConnections() {
        val connectorThread = Thread({
            try {
                while (!Thread.interrupted() && !isClosed) {
                    val connection = TcpConnection(serverSocket!!.accept(), listener, this)
                    listener.connected(connection)
                    connectionList!!.add(connection)
                    Thread.yield()
                }
            } catch (e: IOException) {
                System.err.println("TcpServer (connect client error): " + e.message)
            }
        }, "TcpServer-Thread")
        connectorThread.priority = Thread.MIN_PRIORITY
        connectorThread.isDaemon = true
        connectorThread.start()
    }

    fun broadcast(packet: ByteArray?) {
        for (channel in connectionList!!) channel.send(packet)
    }

    @kotlin.jvm.Synchronized
    public override fun disconnected(connection: TcpConnection) {
        listener.disconnected(connection)
        connectionList!!.remove(connection)
        connection.close()
    }

    val connections: Collection<TcpConnection>?
        get() = connectionList

    override fun close() {
        if (isClosed) return
        for (connection in connectionList!!) connection.close()
        connectionList!!.clear()
        isClosed = true
        Utils.close(serverSocket)
    }
}