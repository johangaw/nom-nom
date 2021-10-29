package com.example.eatyeaty

import com.example.eatyeaty.repositories.RecipeUrlDAO
import com.example.eatyeaty.repositories.getRecipeDataString
import com.example.eatyeaty.repositories.parseRecipeData
import org.json.JSONArray
import org.json.JSONObject
import org.jsoup.Jsoup
import org.junit.Test

import org.junit.Assert.*

fun getHTML(json: String): String = """<html><head><script type="application/ld+json">${json}</script></head></html>"""

class RecipeUrlRepositoryTest {
    @Test
    fun getRecipeDataString_whenNoCorrectScriptTagsIsFound_ItReturnsNull() {
        // Arrange
        val doc = Jsoup.parse("<html><head> </head></html>")

        // Act
        val res = getRecipeDataString(doc)

        // Assert
        assertEquals(null, res)
    }

    @Test
    fun getRecipeDataString_whenAnEmptyScriptTagsIsFound_ItReturnsNull() {
        // Arrange
        val doc = Jsoup.parse(getHTML(""))

        // Act
        val res = getRecipeDataString(doc)

        // Assert
        assertEquals(null, res)
    }

    @Test
    fun getRecipeDataString_whenTheScriptTagContainsOneNoneRecipeObject_ItReturnsNull() {
        // Arrange
        val doc = Jsoup.parse(getHTML("""{"ugg": 1337}"""))

        // Act
        val res = getRecipeDataString(doc)

        // Assert
        assertEquals(null, res)
    }

    @Test
    fun getRecipeDataString_whenTheScriptTagContainsOneRecipeObject_ItReturnsTheRecipeJSONObject() {
        // Arrange
        val doc = Jsoup.parse(getHTML("""{"@type": "Recipe", "name": "Klassisk lasagne"}"""))

        // Act
        val res = getRecipeDataString(doc)

        // Assert
        assertEquals("Recipe", res?.getString("@type"))
        assertEquals("Klassisk lasagne", res?.getString("name"))
    }

    @Test
    fun getRecipeDataString_whenTheScriptTagContainsAnArrayOfNoneRecipeObject_ItReturnsNull() {
        // Arrange
        val doc = Jsoup.parse(getHTML("""[{"@type": "ugg"}, {}]"""))

        // Act
        val res = getRecipeDataString(doc)

        // Assert
        assertNull(res)
    }

    @Test
    fun getRecipeDataString_whenTheScriptTagContainsAnArrayWithOneRecipeObject_ItReturnsTheRecipeJSONObject() {
        // Arrange
        val doc = Jsoup.parse(getHTML("""[{"@type": "ugg"}, {"@type": "Recipe", "name": "Klassisk lasagne"}]"""))

        // Act
        val res = getRecipeDataString(doc)

        // Assert
        assertEquals("Recipe", res?.getString("@type"))
        assertEquals("Klassisk lasagne", res?.getString("name"))
    }

    @Test
    fun parseRecipeData_whenAllFieldsArePresent_ItReturnsACorrespondingDAO() {
        // Arrange
        val input = JSONObject()
        input.put("name", "Steak")
        input.put("image", "https://image.com")
        input.put("recipeInstructions", JSONArray("""["Grill", "Eat"]"""))
        input.put("recipeIngredient", JSONArray("""["Animal corpse", "Fire"]"""))
        val expected = RecipeUrlDAO(
            title = "Steak",
            imageUrl = "https://image.com",
            instructions = listOf("Grill", "Eat"),
            ingredients = listOf("Animal corpse", "Fire")
        )

        // Act
        val result = parseRecipeData(input)

        // Assert
        assertEquals(expected, result)
    }

    @Test
    fun parseRecipeData_whenFieldsAreMissing_ItReturnsACorrespondingDAO() {
        // Arrange
        val input = JSONObject()
        val expected = RecipeUrlDAO(
            title = "",
            imageUrl = "",
            instructions = listOf(),
            ingredients = listOf()
        )

        // Act
        val result = parseRecipeData(input)

        // Assert
        assertEquals(expected, result)
    }
}