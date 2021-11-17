package com.example.nomnom

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.example.nomnom.repositories.createImageFile
import java.io.File


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
            this::onGetContentResult
        )

        takePicture = registry.register(
            "ImagePickerLifecycleMixin.TakePicture",
            owner,
            ActivityResultContracts.TakePicture(),
            this::onTakePictureResult
        )
    }

    private fun onGetContentResult(result: Uri) {
        context.contentResolver.openInputStream(result).use { input ->
            if (input != null) {
                val localFile = createImageFile(context)
                localFile.outputStream().use { output ->
                    input.copyTo(output)
                    localFile.toUri()
                }
            } else {
                null
            }
        }?.let {
            onImageSelected?.invoke(it)
        } ?: Log.d(javaClass.simpleName, "No file found for selected Uri: $result")
    }

    private fun onTakePictureResult(result: Boolean) {
        if (result)
            onImageSelected?.invoke(
                Uri.fromFile(takePictureTargetFile)
            )
        else
            Log.d(javaClass.simpleName, "No image selected")
    }

    override fun pickGalleryImage() {
        getContent.launch("image/*")
    }

    override fun pickCameraImage() {
        val targetFile = createImageFile(context)
        takePictureTargetFile = targetFile

        takePicture.launch(
            FileProvider.getUriForFile(
                context,
                context.getString(R.string.take_picture_authorities),
                targetFile
            )
        )
    }
}