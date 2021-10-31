package com.example.eatyeaty.ui.screen

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.eatyeaty.repositories.Recipe
import com.example.eatyeaty.ui.ShowRecipe

@Composable
fun ShowRecipeScreen(
    recipe: Recipe,
    openUrl: (url: String) -> Unit,
) {

    Surface(
        Modifier.verticalScroll(rememberScrollState())
    ) {
        ShowRecipe(recipe, openUrl)
    }

}