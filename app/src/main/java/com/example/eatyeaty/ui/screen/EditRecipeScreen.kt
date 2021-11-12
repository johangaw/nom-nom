package com.example.eatyeaty.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.eatyeaty.repositories.Recipe
import com.example.eatyeaty.ui.SplashLoader
import com.example.eatyeaty.ui.theme.EditRecipe

@Composable
fun EditRecipeScreen(
    recipe: Recipe,
    onRecipeChange: (Recipe) -> Unit,
    loading: Boolean,
    requestGalleryImage: () -> Unit
) {
    Surface(Modifier.padding(8.dp)) {
        Column(Modifier.verticalScroll(rememberScrollState())) {
            EditRecipe(
                value = recipe,
                onValueChange = onRecipeChange,
                requestGalleryImage = requestGalleryImage
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        if(loading) SplashLoader()
    }
}