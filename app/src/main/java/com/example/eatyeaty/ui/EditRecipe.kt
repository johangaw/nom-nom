package com.example.eatyeaty.ui.theme

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.eatyeaty.R
import com.example.eatyeaty.repositories.Recipe

@Composable
fun EditRecipe(
    value: Recipe,
    onValueChange: (r: Recipe) -> Unit,
    onReloadClick: (url: String) -> Unit,
) {
    Surface(color = MaterialTheme.colors.background) {

        Column(
            Modifier.verticalScroll(rememberScrollState())
        ) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = value.url,
                    onValueChange = { onValueChange(value.copy(url = it)) },
                    Modifier.weight(1f)
                )
                IconButton(onClick = { onReloadClick(value.url) }) {
                    Icon(Icons.Default.Refresh, contentDescription = "")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = value.title,
                onValueChange = { onValueChange(value.copy(title = it)) },
                Modifier.fillMaxWidth(),
                placeholder = { Text("Title") }
            )

            Spacer(modifier = Modifier.height(8.dp))

            ImageWithPlaceholder(
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth(),
                value.image,
                painterResource(id = R.drawable.recipe_placeholder),
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                modifier = Modifier
                    .defaultMinSize(minHeight = 100.dp)
                    .fillMaxWidth(),
                placeholder = { Text("Ingredients") },
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
                placeholder = { Text("Instructions") },
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
}

@Composable
private fun ImageWithPlaceholder(
    modifier: Modifier = Modifier,
    image: Bitmap?,
    placeholder: Painter,
) {
    if (image != null) {
        Image(
            modifier = modifier,
            bitmap = image.asImageBitmap(),
            contentDescription = "",
        )
    } else {
        Image(
            modifier = modifier,
            painter = placeholder,
            contentDescription = "",
        )
    }
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun EditRecipePreview() {
    val (recipe, setRecipe) = remember {
        mutableStateOf(Recipe())
    }
    EatyEatyTheme {
        EditRecipe(
            value = recipe,
            onValueChange = setRecipe,
            onReloadClick = { },
        )
    }
}