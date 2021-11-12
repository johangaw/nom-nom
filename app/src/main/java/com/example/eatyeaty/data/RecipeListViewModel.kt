package com.example.eatyeaty.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.eatyeaty.repositories.Recipe
import com.example.eatyeaty.repositories.RecipeRepository

class RecipeListViewModel : ViewModel() {

    private val repo = RecipeRepository.getInstance()

    val recipes: LiveData<List<Recipe>> = repo.observeAll()
}