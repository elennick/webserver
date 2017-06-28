package com.evanlennick.webserver

import com.evanlennick.webserver.Constants.HTTP_EOL
import com.evanlennick.webserver.exception.NotFoundException
import com.evanlennick.webserver.exception.NotImplementedException
import com.evanlennick.webserver.exception.UnableToParseRequestException
import com.evanlennick.webserver.mimetype.MimeType
import com.evanlennick.webserver.mimetype.MimeTypeUtil
import com.evanlennick.webserver.request.HttpRequest
import com.evanlennick.webserver.response.HttpResponse
import com.evanlennick.webserver.response.HttpResponseBuilder
import com.evanlennick.webserver.response.HttpResponseCode
import com.google.common.io.Files
import java.io.*
import java.net.Socket
import java.util.*

class RequestHandler(private val socket: Socket) {

    @Throws(IOException::class)
    fun go() {
        val start = System.currentTimeMillis()
        val requestId = UUID.randomUUID()

        println("NEW INCOMING REQUEST, ASSIGNING ID: " + requestId)

        val request: HttpRequest
        var responseBuilder: HttpResponseBuilder
        try {
            val requestString = readRequest(socket)
            request = HttpRequest(requestString)
            request.requestId = requestId
            println(request)

            if (request.isGetRequest || request.isHeadRequest) {
                responseBuilder = generateGetOrHeadResponse(request)
            } else {
                throw NotImplementedException()
            }
        } catch (e: NotImplementedException) {
            responseBuilder = HttpResponseBuilder()
                    .code(HttpResponseCode.NOT_IMPLEMENTED)
        } catch (e: NotFoundException) {
            responseBuilder = HttpResponseBuilder()
                    .code(HttpResponseCode.NOT_FOUND)
        } catch (e: UnableToParseRequestException) {
            responseBuilder = HttpResponseBuilder()
                    .code(HttpResponseCode.BAD_REQUEST)
        } catch (e: Exception) {
            e.printStackTrace()
            responseBuilder = HttpResponseBuilder()
                    .code(HttpResponseCode.INTERNAL_SERVER_ERROR)
        }

        val response = responseBuilder
                .forRequest(requestId)
                .build()

        println(response)
        writeResponse(response)

        socket.close()

        val stop = System.currentTimeMillis()
        val elapsed = stop - start
        println("Request " + requestId + " took " + elapsed + "ms\n\n")
    }

    @Throws(IOException::class)
    private fun readRequest(socket: Socket): String {
        val isr = InputStreamReader(socket.getInputStream())
        val reader = BufferedReader(isr)

        var line: String? = reader.readLine()
        val request = StringBuffer(line!! + HTTP_EOL)
        while (null != line && !line.isEmpty()) {
            line = reader.readLine()
            request.append(line).append(HTTP_EOL)
        }

        return request.toString()
    }

    @Throws(IOException::class)
    private fun generateGetOrHeadResponse(request: HttpRequest): HttpResponseBuilder {
        val fileLocation = "www/" + request.resource

        val classLoader = javaClass.classLoader
        val locationRequested: String
        var body: ByteArray? = null
        var code: HttpResponseCode
        var contentType: String

        try {
            locationRequested = classLoader.getResource(fileLocation)!!.file
            val file = determineResourceToReturn(locationRequested)

            if (!file.exists() || file.isDirectory) {
                throw NotFoundException()
            }

            code = HttpResponseCode.OK

            val fileExtension = Files.getFileExtension(file.absolutePath)
            contentType = MimeTypeUtil.getMimeTypeStringByFileExtension(fileExtension)

            body = ByteArray(file.length().toInt())
            val bis = BufferedInputStream(FileInputStream(file))
            bis.read(body, 0, body.size)
        } catch (e: NullPointerException) {
            code = HttpResponseCode.NOT_FOUND
            contentType = MimeType.TEXT_PLAIN.mimeTypeString
        }

        val responseBuilder = HttpResponseBuilder()
                .code(code)
                .addHeader("Content-Type", contentType)
                .forRequest(request.requestId)
                .body(body)

        if (request.isHeadRequest) {
            responseBuilder.dontIncludeBody()
        }

        return responseBuilder
    }

    private fun determineResourceToReturn(locationRequested: String): File {
        var file = File(locationRequested)

        if (file.isDirectory) {
            val indexFile = File(file, "index.html")
            if (indexFile.exists() && !indexFile.isDirectory) {
                file = indexFile
            }
        }

        return file
    }

    @Throws(IOException::class)
    private fun writeResponse(response: HttpResponse) {
        val responseByteArray = response.responseAsBytes
        val os = socket.getOutputStream()
        os.write(responseByteArray, 0, responseByteArray.size)
        os.flush()
    }

}
