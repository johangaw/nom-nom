package com.example.eatyeaty.repositories

data class RecipeUrlDAO(
    val title: String = "",
    val instructions: List<String> = listOf(),
    val ingredients: List<String> = listOf(),
    val imageUrl: String = "",
)

data class Recipe(
    val url: String? = null,
    val title: String = "",
    val instructions: List<String> = listOf(),
    val ingredients: List<String> = listOf(),
)