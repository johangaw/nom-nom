package com.example.nomnom.repositories

import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.core.net.toFile
import androidx.core.net.toUri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*


typealias IDeleteImage = suspend (Uri) -> Unit
typealias IDownloadAndStoreImage = suspend (String) -> Uri

fun deleteImage(): IDeleteImage {
    suspend fun inner(uri: Uri) {
        return withContext(Dispatchers.IO) {
            val file = uri.toFile()
            if(file.exists()) file.delete()
        }
    }
    return ::inner
}


fun fetchAndStore(context: Context): IDownloadAndStoreImage {
    suspend fun inner(url: String): Uri {
        return withContext(Dispatchers.IO) {
            val input = URL(url).openStream()
            input.use {
                val dst = createImageFile(context)
                dst.outputStream().use { output ->
                    input.copyTo(output)
                }
                dst
            }.toUri()
        }
    }
    return ::inner
}

fun createImageFile(context: Context): File {
    val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile(
        "JPEG_${timeStamp}_",
        ".jpg",
        storageDir
    )
}