package com.example.nomnom.repositories

import android.util.Log
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements

operator fun JSONArray.iterator(): Iterable<Any> =
    (0 until length())
        .asSequence()
        .map { get(it) }
        .asIterable()

fun JSONObject.getString(key: String, fallback: String): String =
    if (has(key)) getString(key) else fallback


fun JSONObject.getJSONArray(key: String, fallback: JSONArray): JSONArray =
    if (has(key)) getJSONArray(key) else fallback


suspend fun loadData(url: String): RecipeUrlDAO {
    return withContext(Dispatchers.IO) {
        kotlin.runCatching { Jsoup.connect(url).get() }
            .getOrElse { if(it is CancellationException) throw it else Document("") }
            .let {
                getRecipeDataString(it)
            }
            ?.let {
                parseRecipeData(it).copy(url = url)
            } ?: RecipeUrlDAO()
    }
}


fun getRecipeDataString(doc: Document): JSONObject? {
    val scriptElements: Elements = doc.select("script[type=application/ld+json]")
    return scriptElements
        .map { it.data() }
        .filter { it.isNotEmpty() }
        .flatMap {
            val jsonData = JSONTokener(it).nextValue()
            when (jsonData) {
                is JSONObject -> listOf(jsonData)
                is JSONArray -> jsonData.iterator()
                    .map { if (it is JSONObject) it else null }
                    .filterNotNull()
                else -> listOf()
            }
        }
        .find { it.has("@type") && it.getString("@type", "") == "Recipe" }
}

fun parseRecipeData(json: JSONObject): RecipeUrlDAO {
    return RecipeUrlDAO(
        title = json.getString("name", ""),
        instructions = parseInstructions(json.getJSONArray("recipeInstructions", JSONArray())),
        ingredients = json.getJSONArray("recipeIngredient", JSONArray())
            .iterator()
            .map { it.toString() }
            .toList(),
        imageUrl = json.getString("image", ""),
    )
}

fun listInstructions(item: Any): List<String> {
    return when (item) {
        is JSONArray -> item.iterator().flatMap { listInstructions(it) }
        is JSONObject -> when(item.getString("@type", "")) {
                "HowToSection" -> listInstructions(item.getJSONArray("itemListElement"))
                "HowToStep" -> listInstructions(item.getString("text", ""))
                else -> listInstructions(item.toString())
        }
        is String -> listOf(item)
        else -> listOf(item.toString()).also {
            Log.w("listInstructions","No implementation for type: ${item.javaClass.kotlin.qualifiedName}")
        }
    }
}

fun parseInstructions(array: JSONArray): List<String> {
    return listInstructions(array)
        .flatMap {
            it
                .replace(Regex("<br.*?>"), "\n")
                .replace(Regex("<.*?>"), "")
                .replace("&amp;aring;", "å")
                .replace("&amp;auml;", "ä")
                .replace("&amp;ouml;", "ö")
                .replace("&amp;deg;", "°")
                .split("\n")
        }
        .toList()
}