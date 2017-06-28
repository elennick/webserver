package com.evanlennick.webserver

import com.evanlennick.webserver.Configuration.NUM_OF_REQUEST_THREADS
import com.evanlennick.webserver.Configuration.SOCKET_TIMEOUT_IN_SECONDS
import java.io.IOException
import java.net.ServerSocket
import java.time.Duration
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class Server(private val port: Int) {

    private val pool: ExecutorService = Executors.newFixedThreadPool(NUM_OF_REQUEST_THREADS)

    private var running: Boolean = false

    @Throws(IOException::class)
    fun start() {
        val server: ServerSocket
        try {
            server = ServerSocket(port)
        } catch (e: IOException) {
            println("Error encountered while starting up server: " + e.message)
            e.printStackTrace()
            return
        }

        println("Listening on port $port...\n")
        val socketTimeout = Duration.ofSeconds(SOCKET_TIMEOUT_IN_SECONDS.toLong()).toMillis().toInt()

        running = true
        while (running) {
            val socket = server.accept()
            synchronized(this) {
                pool!!.execute {
                    try {
                        socket.soTimeout = socketTimeout
                        val requestHandler = RequestHandler(socket)
                        requestHandler.go()
                    } catch (e: IOException) {
                        println("Error encountered during request: " + e.message)
                        e.printStackTrace()
                    }
                }
            }
        }

        shutdownAndAwaitTermination(pool)
    }

    private fun shutdownAndAwaitTermination(pool: ExecutorService) {
        pool.shutdown()
        try {
            if (!pool.awaitTermination(10, TimeUnit.SECONDS)) {
                pool.shutdownNow()
                if (!pool.awaitTermination(10, TimeUnit.SECONDS)) {
                    System.err.println("Pool did not terminate")
                }
            }
        } catch (ie: InterruptedException) {
            pool.shutdownNow()
            Thread.currentThread().interrupt()
        }

    }

}
