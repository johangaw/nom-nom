package com.example.eatyeaty

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.example.eatyeaty.ui.theme.EatyEatyTheme
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
                // A surface container using the 'background' color from the theme

            }
        }
    }

    fun downloadRecipe() {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {

                val doc: Document = Jsoup.connect("https://www.ica.se/recept/klassisk-lasagne-679675/").get()
                val scriptElements: Elements = doc.select("script[type=application/ld+json]")
                for (element in scriptElements) {
                    val json = JSONObject(element.data())
                    Log.d(javaClass.simpleName, json.get("@type").toString())
                    Log.d(javaClass.simpleName, json.get("image").toString())
                    Log.d(javaClass.simpleName, json.get("recipeIngredient").toString())

                }
            }
        }
    }
}