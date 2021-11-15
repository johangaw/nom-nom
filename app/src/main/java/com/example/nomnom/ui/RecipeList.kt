package com.example.nomnom.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.nomnom.R
import com.example.nomnom.repositories.Recipe


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RecipeList(
    recipes: List<Recipe>,
    onRecipeSelect: (r: Recipe) -> Unit,
    onRecipeEdit: (r: Recipe) -> Unit,
) {
    Column(
        Modifier.fillMaxSize()
    ) {
        recipes.forEach {
            Card(
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                    .combinedClickable(
                        onClick = { onRecipeSelect(it) },
                        onLongClick = { onRecipeEdit(it) }
                    )
            ) {
                Row(
                    Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .height(40.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ImageWithPlaceholder(
                        modifier = Modifier.width(100.dp),
                        image = it.image,
                        placeholder = painterResource(R.drawable.recipe_placeholder)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = it.title,
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.subtitle1
                    )
                }
            }
        }
    }
}

@Composable
@Preview(showSystemUi = true)
fun RecipeListPreview() {
    RecipeList(
        listOf(
            Recipe(title = "Lasange"),
            Recipe(title = "Pizza"),
        ),
        {},
        {}
    )
}