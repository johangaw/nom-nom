package com.example.eatyeaty.ui

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
import com.example.eatyeaty.R
import com.example.eatyeaty.repositories.Recipe
import com.example.eatyeaty.ui.screen.ShowRecipeScreen

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
        recipe = Recipe(
            title = "Salty caramel cheesecake på kladdkakebotten",
            ingredients = listOf(
                "Kladdkakabotten",
                "2 ägg",
                "1 dl strösocker",
                "100 g smör",
                "1 dl vetemjöl",
                "0,75 dl kakao (av bra kvalitet)",
                "1 msk vaniljsocker",
            ),
            instructions = listOf(
                "Kladdkaka",
                "Smöra kanterna på en rund springform som är 24 cm i diameter med smör och lägg bakplåtspapper i botten. Smält smöret i en kastrull och låt svalna något. Rör ihop ägg och socker. Blanda ihop vetemjöl kakao och vaniljsocker. Sikta ner i äggsmeten och blanda. Häll i det smälta smöret och rör runt med en slickepott till en fin smet. Häll smeten i springformen och försök att fördela så jämnt som möjligt. Lägg åt sidan.",
                "",
                "Salty caramel",
                "Värm sockret på med medelhög värme i en kastrull. Rör om hela tiden tills allt socker har smält (sockret kommer hårdna och bli till små kristaller men den kommer att smälta, se bara till att sockret ej bränns). Tillsätt smöret och låt allt gå ihop. Häll i grädden och låt koka under omrörning i ca 1-2 minuter eller tills härligt seg rinnande och fin färg. Tillsätt salt och rör om. Låt svalna en stund."
            ),
            image = null,
            url = "https://www.koket.se/salty-caramel-cheesecake-pa-kladdkakebotten"
        ),
        openUrl = {}
    )
}