package com.example.eatyeaty.repositories

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

fun JSONObject.getStringOrDefault(key: String): String =
    if (has(key)) getString(key) else ""


fun JSONObject.getJSONArrayOrDefault(key: String): JSONArray =
    if (has(key)) getJSONArray(key) else JSONArray()



fun loadData(url: String): RecipeUrlDAO =
    getRecipeDataString(Jsoup.connect(url).get())?.let {
        parseRecipeData(it)
    } ?: RecipeUrlDAO()


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
        .find { it.has("@type") && it.getString("@type") == "Recipe" }
}

fun parseRecipeData(json: JSONObject): RecipeUrlDAO {
    return RecipeUrlDAO(
        title = json.getStringOrDefault("name"),
        instructions = json.getJSONArrayOrDefault("recipeInstructions").iterator().map { it.toString() }
            .toList(),
        ingredients = json.getJSONArrayOrDefault("recipeIngredient").iterator().map { it.toString() }
            .toList(),
        imageUrl = json.getStringOrDefault("image"),
    )
}