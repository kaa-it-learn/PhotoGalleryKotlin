package ru.tvhelp.akruglov.photogallery

import java.io.ByteArrayOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class FlickrFetchr {
    fun getUrlBytes(urlSpec: String): ByteArray {
        val url = URL(urlSpec)
        val connection = url.openConnection() as HttpURLConnection

        try {
            val out = ByteArrayOutputStream()
            val input = connection.inputStream
            if (connection.responseCode != HttpURLConnection.HTTP_OK) {
                throw IOException(connection.responseMessage + ": with " + urlSpec)
            }
            var bytesRead = 0
            val buffer = ByteArray(1024)
            do {
                bytesRead = input.read(buffer)
                if (bytesRead > 0) {
                    out.write(buffer, 0, bytesRead)
                }
            } while (bytesRead > 0)
            out.close()
            return out.toByteArray()
        } finally {
            connection.disconnect()
        }
    }

    fun getUrlString(urlSpec: String) = String(getUrlBytes(urlSpec))
}