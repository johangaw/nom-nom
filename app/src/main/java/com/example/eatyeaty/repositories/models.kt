package com.example.eatyeaty.repositories

import android.graphics.Bitmap
import java.util.*

data class RecipeUrlDAO(
    val url: String = "",
    val title: String = "",
    val instructions: List<String> = listOf(),
    val ingredients: List<String> = listOf(),
    val imageUrl: String = "",
)

data class Recipe(
    val id: String = UUID.randomUUID().toString(),
    val url: String = "",
    val title: String = "",
    val instructions: List<String> = listOf(),
    val ingredients: List<String> = listOf(),
    val image: Bitmap? = null,
)


suspend fun RecipeUrlDAO.toRecipe(): Recipe {
    return Recipe(
        title = this.title,
        instructions = this.instructions,
        ingredients = this.ingredients,
        image = this.imageUrl.let {
            if (it.isNotEmpty())
                ImageLoader.getInstance().load(it)
            else
                null
        },
        url = this.url
    )
}
