package com.example.eatyeaty.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.eatyeaty.repositories.Recipe

@Composable
fun EditRecipe(
    value: Recipe,
    onValueChange: (r: Recipe) -> Unit,
    onReloadClick: () -> Unit,
) {
    Surface(color = MaterialTheme.colors.background) {

        Column {
            Row(Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = value.url ?: "",
                    onValueChange = { onValueChange(value.copy(url = it)) },
                    Modifier.weight(1f)
                )
                IconButton(onClick = onReloadClick) {
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

            // TODO: use image here
            Surface(
                Modifier
                    .height(200.dp)
                    .fillMaxWidth(), color = Color.Gray
            ) {}

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                modifier = Modifier
                    .height(100.dp)
                    .fillMaxWidth(),
                placeholder={Text("Ingredients")},
                value = value.ingredients.joinToString("\n"),
                onValueChange = {
                    onValueChange(
                        value.copy(
                            ingredients = it.split("\n"))
                    )
                })

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                modifier = Modifier
                    .height(100.dp)
                    .fillMaxWidth(),
                placeholder={Text("Instructions")},
                value = value.instructions.joinToString("\n"),
                onValueChange = {
                    onValueChange(
                        value.copy(
                            instructions = it.split("\n"))
                    )
                })
        }
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