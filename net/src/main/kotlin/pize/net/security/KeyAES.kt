package pize.net.security

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
import pize.files.Resource
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

class KeyAES {
    var key: SecretKey? = null
    private var decryptCipher: Cipher? = null
    private var encryptCipher: Cipher? = null

    constructor(key: SecretKey?) {
        try {
            this.key = key
            initCiphers()
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    constructor(bytes: ByteArray?) {
        try {
            key = SecretKeySpec(bytes, "AES")
            initCiphers()
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    constructor(size: Int) {
        try {
            val generator = KeyGenerator.getInstance("AES")
            generator.init(size)
            key = generator.generateKey()
            initCiphers()
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    @kotlin.Throws(Exception::class)
    private fun initCiphers() {
        decryptCipher = Cipher.getInstance("AES")
        //decryptCipher.init(Cipher.DECRYPT_MODE, key)
        encryptCipher = Cipher.getInstance("AES")
        //encryptCipher.init(Cipher.ENCRYPT_MODE, key)
    }

    fun encrypt(bytes: ByteArray?): ByteArray {
        return try {
            encryptCipher!!.doFinal(bytes)
        } catch (e: IllegalBlockSizeException) {
            throw RuntimeException(e)
        } catch (e: BadPaddingException) {
            throw RuntimeException(e)
        }
    }

    fun decrypt(bytes: ByteArray?): ByteArray? {
        return try {
            decryptCipher!!.doFinal(bytes)
        } catch (ignored: Exception) {
            null
        }
    }

    fun save(resource: Resource) {
        try {
            resource.mkDirsAndFile()
            resource.writer.write(key!!.encoded)
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    companion object {
        fun load(resource: Resource): KeyAES {
            try {
                resource.inStream().use { inStream -> return KeyAES(inStream.readAllBytes()) }
            } catch (e: Exception) {
                throw RuntimeException(e)
            }
        }
    }
}