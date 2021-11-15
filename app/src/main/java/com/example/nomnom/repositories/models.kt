package com.example.nomnom.repositories

import android.graphics.Bitmap
import java.util.*

data class RecipeUrlDAO(
    val url: String = "",
    val title: String = "",
    val instructions: List<String> = listOf(),
    val ingredients: List<String> = listOf(),
    val imageUrl: String = "",
)

data class ImageModel(val url: String = "", val bitmap: Bitmap? = null, val loading: Boolean)

data class Recipe(
    val id: String = UUID.randomUUID().toString(),
    val url: String = "",
    val title: String = "",
    val instructions: List<String> = listOf(),
    val ingredients: List<String> = listOf(),
    val image: ImageModel = ImageModel(loading = false),
)


suspend fun RecipeUrlDAO.toRecipe(): Recipe {
    return Recipe(
        title = this.title,
        instructions = this.instructions,
        ingredients = this.ingredients,
        image = ImageModel(
            bitmap = this.imageUrl.let {
                if (it.isNotEmpty())
                    ImageLoader.getInstance().load(it)
                else
                    null
            },
            url = this.imageUrl,
            loading = false
        )
    )
}
