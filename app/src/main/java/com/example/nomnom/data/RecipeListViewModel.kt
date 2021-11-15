package com.example.nomnom.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.nomnom.repositories.RecipeDBRepository
import com.example.nomnom.repositories.asRecipe
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RecipeListViewModel(
    dbRepository: RecipeDBRepository
) : ViewModel() {

    val recipes: Flow<List<Recipe>> =
        dbRepository.observeAll().map { list -> list.map { it.asRecipe() } }

    class ViewModelFactory(
        private val dbRepository: RecipeDBRepository,
    ) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return RecipeListViewModel(dbRepository) as T
        }
    }
}