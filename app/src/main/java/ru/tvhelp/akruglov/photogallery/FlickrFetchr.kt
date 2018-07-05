package ru.tvhelp.akruglov.photogallery

import android.net.Uri
import android.util.Log
import com.google.gson.Gson
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class FlickrFetchr {
    companion object {
        const val TAG = "FlickrFetchr"
        const val API_KEY = "0291f4cef7edb353289f532acd999f0d"
    }

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

    fun fetchItems(): List<Photo> {
        try {
            val uri = Uri.parse("https://api.flickr.com/services/rest/")
                    .buildUpon()
                    .appendQueryParameter("method", "flickr.photos.getRecent")
                    .appendQueryParameter("api_key", API_KEY)
                    .appendQueryParameter("format", "json")
                    .appendQueryParameter("nojsoncallback", "1")
                    .appendQueryParameter("extras", "url_s")
                    .build().toString()

            val jsonString = getUrlString(uri)
            Log.i(TAG, "Received JSON: " + jsonString)
            val flickrResponse = Gson().fromJson(jsonString, FlickrResponse::class.java)
            return flickrResponse.photos.photo
        } catch (e: Exception) {
            Log.e(TAG, "Failed to fetch items", e)
            return arrayListOf<Photo>()
        }
    }
}