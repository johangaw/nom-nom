package com.example.nomnom.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.nomnom.repositories.RecipeRepository

class RecipeListViewModel : ViewModel() {

    private val repo = RecipeRepository.getInstance()

    val recipes: LiveData<List<Recipe>> = repo.observeAll()
}