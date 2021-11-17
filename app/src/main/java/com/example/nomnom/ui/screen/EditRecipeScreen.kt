package com.example.nomnom.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.nomnom.data.EditRecipeViewModel
import com.example.nomnom.data.Recipe
import com.example.nomnom.fixtures.recipe1
import com.example.nomnom.ui.SplashLoader
import com.example.nomnom.ui.EditRecipe
import com.example.nomnom.ui.theme.NomNomTheme

@Composable
fun EditRecipeScreen(
    recipe: Recipe,
    onRecipeChange: (Recipe) -> Unit,
    onRecipeDelete: (Recipe) -> Unit,
    loading: Boolean,
    requestGalleryImage: () -> Unit,
    requestCameraImage: () -> Unit,
) {
    Surface(Modifier.padding(8.dp)) {
        Column(Modifier.verticalScroll(rememberScrollState())) {
            EditRecipe(
                value = recipe,
                onValueChange = onRecipeChange,
                requestGalleryImage = requestGalleryImage,
                requestCameraImage = requestCameraImage,

                )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { onRecipeDelete(recipe) },
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.error)
            ) {
                Text(text = "Delete")
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        if (loading) SplashLoader()
    }
}

@Composable
@Preview(heightDp = 1200)
fun EditRecipeScreenPreview() {
    NomNomTheme {
        EditRecipeScreen(
            recipe = recipe1,
            onRecipeChange = {},
            onRecipeDelete = {},
            loading = false,
            requestGalleryImage = {},
            requestCameraImage = {}
        )
    }
}