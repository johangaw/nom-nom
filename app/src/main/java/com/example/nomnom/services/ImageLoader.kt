package com.example.nomnom.services

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL


class ImageLoader {

    suspend fun load(url: String): Bitmap {
        return withContext(Dispatchers.IO) {
            val input = URL(url).openStream()
            BitmapFactory.decodeStream(input)
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