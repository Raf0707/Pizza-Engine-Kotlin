package pize.tests.voxelgame

import pize.tests.voxelgame.server.Server

object ServerMain {
    @JvmStatic
    fun main(args: Array<String>) {
        val server: Server = object : Server() {
            override fun run() {
                super.run()
            }
        }
        server.run()
        while (!Thread.interrupted());
    }
}
