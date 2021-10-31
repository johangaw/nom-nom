package com.example.eatyeaty.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.eatyeaty.R
import com.example.eatyeaty.repositories.Recipe
import com.example.eatyeaty.ui.ImageWithPlaceholder

@Composable
fun EditRecipe(
    value: Recipe,
    onValueChange: (r: Recipe) -> Unit,
) {
    Column() {
        OutlinedTextField(
            value = value.title,
            onValueChange = { onValueChange(value.copy(title = it)) },
            Modifier.fillMaxWidth(),
            label = { Text("Title") },
            singleLine = true,
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
    EatyEatyTheme {
        EditRecipe(
            value = recipe,
            onValueChange = setRecipe,
        )
    }
}