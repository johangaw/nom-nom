package com.example.eatyeaty

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.eatyeaty.ui.App
import android.content.Intent
import android.net.Uri
import androidx.activity.viewModels
import com.example.eatyeaty.data.RecipeListViewModel
import com.example.eatyeaty.data.EditRecipeViewModel
import com.example.eatyeaty.services.ImageDecoder


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val recipeListModel: RecipeListViewModel by viewModels()

        val imagePicker = ImagePickerLifecycleObserver(this, this.activityResultRegistry)
        lifecycle.addObserver(imagePicker)
        val editModel: EditRecipeViewModel by viewModels {
            val imageDecoder = ImageDecoder(this.contentResolver)
            EditRecipeViewModel.ViewModelFactory(imagePicker, imageDecoder)
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