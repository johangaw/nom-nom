package com.example.nomnom.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.nomnom.R
import com.example.nomnom.data.Recipe
import com.example.nomnom.ui.theme.NomNomTheme

@Composable
fun EditRecipe(
    value: Recipe,
    onValueChange: (r: Recipe) -> Unit,
    requestGalleryImage: () -> Unit,
    requestCameraImage: () -> Unit,
) {
    Column {
        OutlinedTextField(
            value = value.title,
            onValueChange = { onValueChange(value.copy(title = it)) },
            Modifier.fillMaxWidth(),
            label = { Text("Title") },
            singleLine = true,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth()
        ) {
            ImageWithPlaceholder(
                modifier = Modifier.fillMaxSize(),
                value.imageUri,
                painterResource(id = R.drawable.recipe_placeholder),
            )
            Row(
                Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.8f)),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                IconButton(onClick = requestCameraImage) {
                    Icon(
                        Icons.Default.CameraAlt,
                        contentDescription = "",
                        Modifier.size(100.dp),
                        Color.White
                    )
                }
                IconButton(onClick = requestGalleryImage) {
                    Icon(
                        Icons.Default.PhotoAlbum,
                        contentDescription = "",
                        Modifier.size(100.dp),
                        Color.White
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            modifier = Modifier
                .defaultMinSize(minHeight = 100.dp)
                .fillMaxWidth(),
            label = { Text("Ingredients") },
            value = value.ingredients.joinToString("\n"),
            onValueChange = {
                onValueChange(
                    value.copy(
                        ingredients = it.split("\n")
                    )
                )
            })

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            modifier = Modifier
                .defaultMinSize(minHeight = 100.dp)
                .fillMaxWidth(),
            label = { Text("Instructions") },
            value = value.instructions.joinToString("\n"),
            onValueChange = {
                onValueChange(
                    value.copy(
                        instructions = it.split("\n")
                    )
                )
            })
    }
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun EditRecipePreview() {
    val (recipe, setRecipe) = remember {
        mutableStateOf(Recipe())
    }
    NomNomTheme {
        EditRecipe(
            value = recipe,
            onValueChange = setRecipe,
            requestGalleryImage = {},
            requestCameraImage = {},
        )
    }
}