package pize.net.tcp

abstract class TcpDisconnector {
    abstract fun disconnected(connection: TcpConnection)
}