package com.example.nomnom.services

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL


class ImageLoader {

    suspend fun fetchAndStore(context: Context, url: String): Uri  {
        return withContext(Dispatchers.IO) {
            val dst = createImageFile(context)
            val input = URL(url).openStream()
            input.use {
                dst.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
            dst.toUri()
        }
    }

    companion object {

        @Volatile
        private var INSTANCE: ImageLoader? = null

        fun getInstance(): ImageLoader {
            return INSTANCE ?: synchronized(ImageLoader.javaClass) {
                INSTANCE = ImageLoader()
                INSTANCE!!
            }
        }
    }


}