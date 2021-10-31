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
    fun `getRecipeDataString -- when no correct script tags are found -- it returns null`() {
        // Arrange
        val doc = Jsoup.parse("<html><head> </head></html>")

        // Act
        val res = getRecipeDataString(doc)

        // Assert
        assertEquals(null, res)
    }

    @Test
    fun `getRecipeDataString -- when an empty script tags is found -- it returns null`() {
        // Arrange
        val doc = Jsoup.parse(getHTML(""))

        // Act
        val res = getRecipeDataString(doc)

        // Assert
        assertEquals(null, res)
    }

    @Test
    fun `getRecipeDataString -- when the script tag contains one none recipe object -- it returns null`() {
        // Arrange
        val doc = Jsoup.parse(getHTML("""{"ugg": 1337}"""))

        // Act
        val res = getRecipeDataString(doc)

        // Assert
        assertEquals(null, res)
    }

    @Test
    fun `getRecipeDataString -- when the script tag contains one recipe object -- it returns the recipe JSONObject`() {
        // Arrange
        val doc = Jsoup.parse(getHTML("""{"@type": "Recipe", "name": "Klassisk lasagne"}"""))

        // Act
        val res = getRecipeDataString(doc)

        // Assert
        assertEquals("Recipe", res?.getString("@type"))
        assertEquals("Klassisk lasagne", res?.getString("name"))
    }

    @Test
    fun `getRecipeDataString -- when the script tag contains an array of none recipe Object -- it returns null`() {
        // Arrange
        val doc = Jsoup.parse(getHTML("""[{"@type": "ugg"}, {}]"""))

        // Act
        val res = getRecipeDataString(doc)

        // Assert
        assertNull(res)
    }

    @Test
    fun `getRecipeDataString -- when the script tag contains an array with one recipe object -- itreturns the recipe JSONObject`() {
        // Arrange
        val doc = Jsoup.parse(getHTML("""[{"@type": "ugg"}, {"@type": "Recipe", "name": "Klassisk lasagne"}]"""))

        // Act
        val res = getRecipeDataString(doc)

        // Assert
        assertEquals("Recipe", res?.getString("@type"))
        assertEquals("Klassisk lasagne", res?.getString("name"))
    }

    @Test
    fun `parseRecipeData -- when all fields are present -- it returns a corresponding DAO`() {
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
    fun `parseRecipeData -- when fields are missing -- it returns a corresponding DAO`() {
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