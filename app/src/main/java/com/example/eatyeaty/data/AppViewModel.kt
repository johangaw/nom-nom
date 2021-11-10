package com.example.eatyeaty.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.eatyeaty.fixtures.recipe1
import com.example.eatyeaty.fixtures.recipe2
import com.example.eatyeaty.repositories.Recipe

class AppViewModel : ViewModel() {

    private val _recipes = MutableLiveData(
        listOf(
            recipe1,
            recipe2,
        )
    )
    val recipes: LiveData<List<Recipe>> = _recipes

    fun createRecipe(recipe: Recipe) {
        _recipes.postValue((_recipes.value ?: listOf()) + recipe)
    }

    fun getRecipe(id: String): Recipe? {
        return _recipes.value?.find { it.id == id }
    }

    fun updateRecipe(recipe: Recipe) {
        _recipes.postValue(
            _recipes.value?.map { if(it.id === recipe.id) recipe else it } ?: listOf()
        )
    }
}