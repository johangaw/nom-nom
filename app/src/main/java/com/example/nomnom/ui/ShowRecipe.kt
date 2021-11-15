package com.example.nomnom.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.nomnom.R
import com.example.nomnom.fixtures.recipe1
import com.example.nomnom.repositories.Recipe

@Composable
fun ShowRecipe(
    recipe: Recipe,
    openUrl: (url: String) -> Unit
) {
    val horizontalPadding = 16.dp
    Column {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = horizontalPadding),
            text = recipe.title,
            style = MaterialTheme.typography.h4,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        ImageWithPlaceholder(
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth(),
            image = recipe.image,
            placeholder = painterResource(R.drawable.recipe_placeholder)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Column(Modifier.padding(horizontal = horizontalPadding)) {
            Text(text = "Ingredients", style = MaterialTheme.typography.h5)
            Text(text = recipe.ingredients.joinToString("\n"))

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Instructions", style = MaterialTheme.typography.h5)
            Text(text = recipe.instructions.joinToString("\n"))

            Spacer(modifier = Modifier.height(16.dp))

            if (recipe.url.isNotEmpty()) {
                TextButton(
                    onClick = { openUrl(recipe.url) },
                ) {
                    Text(text = "Original Recipe", style = MaterialTheme.typography.caption)
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Preview(showBackground = true, heightDp = 1000)
@Composable
fun ShowRecipePreview() {
    ShowRecipe(
        recipe = recipe1,
        openUrl = {}
    )
}