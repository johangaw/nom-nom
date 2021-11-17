package com.example.nomnom.data

import android.net.Uri
import androidx.lifecycle.*
import com.example.nomnom.IImagePickerLifecycleObserver
import com.example.nomnom.repositories.*
import kotlinx.coroutines.launch

class EditRecipeViewModel(
    private val imagePicker: IImagePickerLifecycleObserver,
    private val dbRepository: RecipeDBRepository,
    private val deleteImage: IDeleteImage,
) : ViewModel() {

    private val _recipe = MutableLiveData(Recipe())
    val recipe: LiveData<Recipe> = _recipe

    init {
        imagePicker.onImageSelected = this::imageSelected
    }

    suspend fun createRecipe(recipe: Recipe): Recipe {
        val id = dbRepository.create(recipe.asRecipeEntity())
        return dbRepository.getOne(id.toInt()).asRecipe()
    }

    suspend fun selectRecipe(id: String) {
        _recipe.postValue(dbRepository.getOne(id.toInt()).asRecipe())
    }

    fun updateRecipe(recipe: Recipe) {
        val oldImageUri = _recipe.value?.imageUri
        _recipe.postValue(recipe)
        if (recipe.id != 0) viewModelScope.launch {
            dbRepository.update(recipe.asRecipeEntity())
            if (oldImageUri !== recipe.imageUri) {
                deleteImage(Uri.parse(oldImageUri))
            }
        }
    }

    fun requestGalleryImage() {
        imagePicker.pickGalleryImage()
    }

    fun requestCameraImage() {
        imagePicker.pickCameraImage()
    }

    private fun imageSelected(uri: Uri) {
        _recipe.value?.let {
            val oldImageUri = it.imageUri
            val recipe = it.copy(
                imageUri = uri.toString(),
            )
            _recipe.postValue(recipe)
            if (recipe.id != 0) viewModelScope.launch {
                dbRepository.update(recipe.asRecipeEntity())
                if (oldImageUri !== recipe.imageUri) {
                    deleteImage(Uri.parse(oldImageUri))
                }
            }
        }
    }

    class ViewModelFactory(
        private val imagePicker: IImagePickerLifecycleObserver,
        private val dbRepository: RecipeDBRepository,
        private val deleteImage: IDeleteImage,
    ) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return EditRecipeViewModel(imagePicker, dbRepository, deleteImage) as T
        }
    }
}