package com.example.nomnom.data

import android.graphics.Bitmap

data class RecipeUrlDAO(
    val url: String = "",
    val title: String = "",
    val instructions: List<String> = listOf(),
    val ingredients: List<String> = listOf(),
    val imageUrl: String = "",
)

data class ImageModel(
    val uri: String = "",
    val bitmap: Bitmap? = null,
    val loading: Boolean
)

data class Recipe(
    val id: Int = 0,
    val url: String = "",
    val title: String = "",
    val instructions: List<String> = listOf(),
    val ingredients: List<String> = listOf(),
    val imageUri: String = "",
)


fun RecipeUrlDAO.asRecipe(imageUri: String): Recipe {
    return Recipe(
        title = this.title,
        url = this.url,
        instructions = this.instructions,
        ingredients = this.ingredients,
        imageUri = imageUri
    )
}
