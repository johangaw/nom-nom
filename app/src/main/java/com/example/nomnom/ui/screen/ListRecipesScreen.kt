package com.example.nomnom.ui

import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.nomnom.fixtures.recipe1
import com.example.nomnom.fixtures.recipe2
import com.example.nomnom.repositories.Recipe

@Composable
fun ListScreen(
    recipes: List<Recipe>,
    onCreateClick: () -> Unit,
    onRecipeSelect: (r: Recipe) -> Unit,
    onRecipeEdit: (r: Recipe) -> Unit,
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onCreateClick) {
                Icon(Icons.Default.Add, contentDescription = "")
            }
        }
    ) {
        RecipeList(
            recipes = recipes,
            onRecipeSelect = onRecipeSelect,
            onRecipeEdit = onRecipeEdit,
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun ListScreenPreview() {
    ListScreen(
        recipes = listOf(
            recipe1,
            recipe2,
        ),
        onCreateClick = {},
        onRecipeSelect = {},
        onRecipeEdit = {},
    )
}