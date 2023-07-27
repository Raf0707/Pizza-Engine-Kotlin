package pize.net.tcp.packet

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
import java.security.KeyPairGenerator
import java.security.NoSuchAlgorithmException
import java.security.PublicKey
import java.security.KeyFactory
import java.security.spec.X509EncodedKeySpec
import java.security.PrivateKey
import java.security.spec.PKCS8EncodedKeySpec

abstract class IPacket<L : PacketHandler?>(packetID: Int) {
    val packetID: Byte
    fun write(connection: TcpConnection?) {
        if (connection == null) return
        connection.send(PacketWriter { dataStream: PizeOutputStream? ->
            try {
                dataStream!!.writeByte(packetID.toInt())
                write(dataStream)
                // System.out.println("Write packet " + packetID + " (size: " + dataStream.size() + ")");
            } catch (e: IOException) {
                e.printStackTrace()
            }
        })
    }

    @kotlin.Throws(IOException::class)
    protected abstract fun write(stream: PizeOutputStream?) // For a sender
    @kotlin.Throws(IOException::class)
    abstract fun read(stream: PizeInputStream?) // For a receiver
    open fun handle(packetListener: L) {}

    init {
        this.packetID = packetID.toByte()
    }
}