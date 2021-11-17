package com.example.nomnom

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.nomnom.ui.App
import android.content.Intent
import android.net.Uri
import androidx.activity.viewModels
import com.example.nomnom.data.RecipeListViewModel
import com.example.nomnom.data.EditRecipeViewModel
import com.example.nomnom.repositories.RecipeDatabase
import com.example.nomnom.repositories.deleteImage


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val recipeListModel: RecipeListViewModel by viewModels {
            RecipeListViewModel.ViewModelFactory(
                RecipeDatabase.getInstance(this).recipeRepository()
            )
        }

        val imagePicker = ImagePickerLifecycleObserver(this, this.activityResultRegistry)
        lifecycle.addObserver(imagePicker)
        val editModel: EditRecipeViewModel by viewModels {
            EditRecipeViewModel.ViewModelFactory(
                imagePicker,
                RecipeDatabase.getInstance(this).recipeRepository(),
                deleteImage()
            )
        }

        setContent {
            App(
                openUrl = this::openUrl,
                recipeListModel = recipeListModel,
                editModel = editModel,
            )
        }
    }

    fun openUrl(url: String) {
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse(url)
            )
        )
    }
}