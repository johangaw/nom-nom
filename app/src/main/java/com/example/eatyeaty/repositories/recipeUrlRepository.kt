package com.example.eatyeaty.repositories

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
        getRecipeDataString(Jsoup.connect(url).get())?.let {
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
        instructions = json.getJSONArray("recipeInstructions", JSONArray())
            .iterator()
            .map {
                if (it is JSONObject && it.getString("@type", "") == "HowToStep")
                    it.getString("text", "")
                else
                    it.toString()
            }
            .flatMap {
                it
                    .replace(Regex("<br.*?>"), "\n")
                    .replace(Regex("<.*?>"), "")
                    .split("\n")
            }
            .toList(),
        ingredients = json.getJSONArray("recipeIngredient", JSONArray())
            .iterator()
            .map { it.toString() }
            .toList(),
        imageUrl = json.getString("image", ""),
    )
}