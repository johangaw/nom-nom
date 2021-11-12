package com.example.eatyeaty

import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

interface IImagePickerLifecycleObserver {
    var onImageSelected: ((Uri) -> Unit)?
    fun pickGalleryImage()
    fun pickCameraImage()
}

class ImagePickerLifecycleObserver(
    private val registry: ActivityResultRegistry,
    override var onImageSelected: ((Uri) -> Unit)? = null,
) :
    DefaultLifecycleObserver, IImagePickerLifecycleObserver {
    lateinit var getContent: ActivityResultLauncher<String>

    override fun onCreate(owner: LifecycleOwner) {
        getContent = registry.register(
            "ImagePickerLifecycleMixin.GetContent",
            owner,
            ActivityResultContracts.GetContent(),
            { onImageSelected?.invoke(it) }
        )
    }

    override fun pickGalleryImage() {
        getContent.launch("image/*")
    }

    override fun pickCameraImage() {

    }
}