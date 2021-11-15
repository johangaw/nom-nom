package com.example.nomnom.services

import android.content.Context
import android.os.Environment
import kotlinx.coroutines.CancellationException
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

fun <T> Result<T>.throwOnCancellation(): Result<T> {
    return onFailure { if(it is CancellationException) throw it else it }
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