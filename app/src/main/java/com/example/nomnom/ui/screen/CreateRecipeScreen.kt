package com.example.nomnom.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.nomnom.fixtures.recipe1
import com.example.nomnom.repositories.Recipe
import com.example.nomnom.ui.SplashLoader
import com.example.nomnom.ui.theme.NomNomTheme
import com.example.nomnom.ui.EditRecipe

@Composable
fun CreateRecipeScreen(
    recipe: Recipe,
    onRecipeChange: (recipe: Recipe) -> Unit,
    onCreateClick: () -> Unit,
    requestGalleryImage: () -> Unit,
    requestCameraImage: () -> Unit,
    loading: Boolean,
) {
    Surface(Modifier.padding(8.dp)) {
        Column(
            Modifier.verticalScroll(rememberScrollState())
        ) {
            EditRecipe(
                value = recipe,
                onValueChange = onRecipeChange,
                requestGalleryImage = requestGalleryImage,
                requestCameraImage = requestCameraImage
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onCreateClick, Modifier.fillMaxWidth()) {
                Text(text = "Create")
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        if (loading)
            SplashLoader()
    }
}


@Preview
@Composable
fun CreateRecipeScreenPreview__whenLoading() {
    NomNomTheme {
        CreateRecipeScreen(
            recipe = recipe1,
            onRecipeChange = {},
            onCreateClick = { },
            requestGalleryImage = {},
            requestCameraImage = {},
            loading = true,
        )
    }
}

@Preview
@Composable
fun CreateRecipeScreenPreview() {
    NomNomTheme {
        CreateRecipeScreen(
            recipe = recipe1,
            onRecipeChange = {},
            onCreateClick = { },
            requestGalleryImage = {},
            requestCameraImage = {},
            loading = false,
        )
    }
}