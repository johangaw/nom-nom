package com.example.nomnom.data

import android.net.Uri
import androidx.lifecycle.*
import com.example.nomnom.IImagePickerLifecycleObserver
import com.example.nomnom.repositories.*
import com.example.nomnom.services.IImageDecoder
import com.example.nomnom.services.ImageDecoder
import kotlinx.coroutines.launch

class EditRecipeViewModel(
    private val imageDecoder: IImageDecoder,
    private val imagePicker: IImagePickerLifecycleObserver,
    private val dbRepository: RecipeDBRepository,
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
        _recipe.postValue(recipe)
        viewModelScope.launch {
            // TODO: remove old image from disc if changed
            dbRepository.update(recipe.asRecipeEntity())
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
            val recipe = it.copy(
                imageUri = uri.toString(),
            )
            _recipe.postValue(recipe)
            if (recipe.id != 0)
                viewModelScope.launch {
                    // TODO: remove old image from disc if changed
                    dbRepository.update(recipe.asRecipeEntity())
                }
        }
    }

    class ViewModelFactory(
        private val imagePicker: IImagePickerLifecycleObserver,
        private val imageDecoder: ImageDecoder,
        private val dbRepository: RecipeDBRepository,
    ) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return EditRecipeViewModel(imageDecoder, imagePicker, dbRepository) as T
        }
    }
}