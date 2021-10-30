package com.example.eatyeaty.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.eatyeaty.repositories.Recipe
import com.example.eatyeaty.ui.theme.EditRecipe

@Composable
fun CreateRecipeScreen(
    recipe: Recipe,
    onRecipeChange: (recipe: Recipe) -> Unit,
    onCreateClick: () -> Unit
) {
    Surface(Modifier.padding(8.dp)) {
        Column(
            Modifier.verticalScroll(rememberScrollState())
        ) {
            EditRecipe(value = recipe, onValueChange = onRecipeChange)
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onCreateClick, Modifier.fillMaxWidth()) {
                Text(text = "Create")
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}