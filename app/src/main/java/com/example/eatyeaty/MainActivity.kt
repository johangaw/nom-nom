package com.example.eatyeaty

import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.util.JsonReader
import android.util.Log
import android.util.Xml
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.example.eatyeaty.repositories.ImageLoader
import com.example.eatyeaty.repositories.Recipe
import com.example.eatyeaty.repositories.loadData
import com.example.eatyeaty.ui.theme.EatyEatyTheme
import com.example.eatyeaty.ui.theme.EditRecipe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import org.json.JSONObject


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            EatyEatyTheme {

                val (recipe, setRecipe) = remember {
                    mutableStateOf(Recipe())
                }

                val scope = rememberCoroutineScope()

                EditRecipe(value = recipe, onValueChange = setRecipe, onReloadClick = {
                    scope.launch {
                        val data = loadData(recipe.url)
                        setRecipe(recipe.copy(
                            title = data.title,
                            instructions = data.instructions,
                            ingredients = data.ingredients,
                            image = data.imageUrl.let {
                                ImageLoader.getInstance().load(it)
                            }
                        ))
                    }
                })

            }
        }
    }
}