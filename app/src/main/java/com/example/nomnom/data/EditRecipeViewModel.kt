package com.example.nomnom.data

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.nomnom.IImagePickerLifecycleObserver
import com.example.nomnom.repositories.RecipeRepository
import com.example.nomnom.services.IImageDecoder
import com.example.nomnom.services.ImageDecoder

class EditRecipeViewModel(
    private val imageDecoder: IImageDecoder,
    private val imagePicker: IImagePickerLifecycleObserver
) : ViewModel() {

    private val repo = RecipeRepository.getInstance()
    private val _recipe = MutableLiveData(Recipe())
    val recipe: LiveData<Recipe> = _recipe

    init {
        imagePicker.onImageSelected = this::imageSelected
    }

    fun createRecipe(recipe: Recipe) {
        repo.create(recipe)
    }

    fun selectRecipe(id: String) {
        _recipe.postValue(null)
        _recipe.postValue(repo.getOne(id))
    }

    fun updateRecipe(recipe: Recipe) {
        repo.update(recipe)
        _recipe.postValue(recipe)
    }

    fun requestGalleryImage() {
        imagePicker.pickGalleryImage()
    }

    fun requestCameraImage() {
        imagePicker.pickCameraImage()
    }

    private fun imageSelected(uri: Uri) {
        _recipe.value?.let {
            val recipe = it.copy(
                image = ImageModel(
                    bitmap = imageDecoder.decode(uri),
                    url = uri.toString(),
                    loading = false
                )
            )
            repo.update(recipe)
            _recipe.postValue(recipe)
        }
    }

    class ViewModelFactory(
        private val imagePicker: IImagePickerLifecycleObserver,
        private val imageDecoder: ImageDecoder
    ) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return EditRecipeViewModel(imageDecoder, imagePicker) as T
        }
    }
}