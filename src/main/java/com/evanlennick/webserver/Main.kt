package com.evanlennick.webserver

import java.io.IOException

import com.evanlennick.webserver.Configuration.DEFAULT_PORT

object Main {

    lateinit var server: Server

    var isTestMode: Boolean = false

    @JvmStatic fun main(args: Array<String>) {
        val port = getPort(args)

        Thread {
            server = Server(port)
            try {
                server.start()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }.start()
    }

    @JvmStatic fun stop() {
        server.stop()
    }

    private fun getPort(args: Array<String>): Int {
        if (args.size > 0) {
            return Integer.parseInt(args[0])
        } else {
            return DEFAULT_PORT
        }
    }

}
