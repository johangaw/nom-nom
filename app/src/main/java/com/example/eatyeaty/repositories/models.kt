package com.example.eatyeaty.repositories

import android.graphics.Bitmap

data class RecipeUrlDAO(
    val title: String = "",
    val instructions: List<String> = listOf(),
    val ingredients: List<String> = listOf(),
    val imageUrl: String = "",
)

data class Recipe(
    val url: String = "",
    val title: String = "",
    val instructions: List<String> = listOf(),
    val ingredients: List<String> = listOf(),
    val image: Bitmap? = null,
)