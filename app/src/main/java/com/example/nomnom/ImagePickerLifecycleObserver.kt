package com.example.nomnom

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


interface IImagePickerLifecycleObserver {
    var onImageSelected: ((Uri) -> Unit)?
    fun pickGalleryImage()
    fun pickCameraImage()
}

class ImagePickerLifecycleObserver(
    private val context: Context,
    private val registry: ActivityResultRegistry,
    override var onImageSelected: ((Uri) -> Unit)? = null,
) :
    DefaultLifecycleObserver, IImagePickerLifecycleObserver {
    private lateinit var getContent: ActivityResultLauncher<String>
    private lateinit var takePicture: ActivityResultLauncher<Uri>

    private var takePictureTargetFile: File? = null


    override fun onCreate(owner: LifecycleOwner) {
        getContent = registry.register(
            "ImagePickerLifecycleMixin.GetContent",
            owner,
            ActivityResultContracts.GetContent(),
            { onImageSelected?.invoke(it) }
        )

        takePicture = registry.register(
            "ImagePickerLifecycleMixin.TakePicture",
            owner,
            ActivityResultContracts.TakePicture(),
            {
                if (it)
                    onImageSelected?.invoke(
                        Uri.fromFile(takePictureTargetFile)
                    )
                else
                    Log.d(javaClass.simpleName, "No image selected")
            }
        )
    }

    override fun pickGalleryImage() {
        getContent.launch("image/*")
    }

    override fun pickCameraImage() {
        val targetFile = createImageFile()
        takePictureTargetFile = targetFile

        takePicture.launch(
            FileProvider.getUriForFile(
                context,
                context.getString(R.string.take_picture_authorities),
                targetFile
            )
        )
    }

    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        )
    }
}