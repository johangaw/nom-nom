package com.example.nomnom.ui.screen

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.nomnom.data.Recipe
import com.example.nomnom.ui.ShowRecipe
import com.example.nomnom.ui.SplashLoader

@Composable
fun ShowRecipeScreen(
    recipe: Recipe,
    openUrl: (url: String) -> Unit,
    loading: Boolean,
) {

    Surface(
        Modifier.verticalScroll(rememberScrollState())
    ) {
        ShowRecipe(recipe, openUrl)
        if(loading)
            SplashLoader()
    }

}