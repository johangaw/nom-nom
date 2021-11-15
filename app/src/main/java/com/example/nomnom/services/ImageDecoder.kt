package com.example.nomnom.services

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri

interface IImageDecoder {
    fun decode(uri: Uri): Bitmap
}

class ImageDecoder(private val contentResolver: ContentResolver) : IImageDecoder {
    override fun decode(uri: Uri): Bitmap {
        return ImageDecoder.decodeBitmap(
            ImageDecoder.createSource(
                contentResolver,
                uri
            )
        )
    }

}