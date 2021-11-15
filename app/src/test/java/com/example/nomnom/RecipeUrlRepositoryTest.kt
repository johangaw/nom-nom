package com.example.nomnom

import com.example.nomnom.repositories.RecipeUrlDAO
import com.example.nomnom.repositories.getRecipeDataString
import com.example.nomnom.repositories.parseRecipeData
import org.json.JSONArray
import org.json.JSONObject
import org.jsoup.Jsoup
import org.junit.Test

import org.junit.Assert.*

fun getHTML(json: String): String =
    """<html><head><script type="application/ld+json">${json}</script></head></html>"""

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
        val doc =
            Jsoup.parse(getHTML("""[{"@type": "ugg"}, {"@type": "Recipe", "name": "Klassisk lasagne"}]"""))

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

    @Test
    fun `parseRecipeData -- when instructions contain HowToSteps -- it returns instructions as strings`() {
        // Arrange
        val input = JSONObject()
        input.put(
            "recipeInstructions",
            JSONArray()
                .put(0, JSONObject().put("@type", "HowToStep").put("text", "Oven on 175"))
                .put(1, JSONObject().put("@type", "HowToStep").put("text", "Cook stuff"))
        )
        val expected = listOf(
            "Oven on 175",
            "Cook stuff",
        )

        // Act
        val result = parseRecipeData(input)

        // Assert
        assertEquals(expected, result.instructions)
    }

    @Test
    fun `parseRecipeData -- when instructions contain HowToSteps with HTML -- it returns strings without HTML`() {
        // Arrange
        val input = JSONObject()
        input.put(
            "recipeInstructions",
            JSONArray()
                .put(0, JSONObject().put("@type", "HowToStep").put("text", "<b>Oven</b> on 175"))
                .put(
                    1,
                    JSONObject().put("@type", "HowToStep").put("text", "Insert<br>Cook<br/>stuff")
                )
        )
        val expected = listOf(
            "Oven on 175",
            "Insert",
            "Cook",
            "stuff",
        )

        // Act
        val result = parseRecipeData(input)

        // Assert
        assertEquals(expected, result.instructions)
    }

    @Test
    fun `parseRecipeData -- when instructions contain HowToSection -- it returns a string of the nested content`() {
        // Arrange
        val input = JSONObject(
            """
            {
                "@type": "Recipe",
                "recipeInstructions": [
                    {
                        "@type": "HowToSection",
                        "itemListElement": [
                            "Step1",
                            {
                                "@type": "HowToStep",
                                "text": "Step2"
                            }
                        ]
                    },
                    {
                        "@type": "HowToSection",
                        "itemListElement": [
                            {
                                "@type": "HowToStep",
                                "text": "Step3"
                            }
                        ]
                    }
                ]
            }
            """.trimIndent()
        )
        val expected = listOf("Step1", "Step2", "Step3")

        // Act
        val result = parseRecipeData(input)


        // Assert
        assertEquals(expected, result.instructions)
    }

    @Test
    fun `parseRecipeData -- when instructions contains HTML encoded characters -- it returns a decoded string`() {
        // Arrange
        val input = JSONObject("""
            {
                "recipeInstructions": [
                    "S&amp;auml;tt ugnen p&amp;aring; 175&amp;deg;C",
                    "Skala och hacka l&amp;ouml;k och vitl&amp;ouml;k",
                    "Fr&amp;auml;s f&amp;auml;rs, l&amp;ouml;k och vitl&amp;ouml;k i oljan i en stekpanna"
                ]
            }
        """.trimIndent())
        val expected = listOf(
            "Sätt ugnen på 175°C",
            "Skala och hacka lök och vitlök",
            "Fräs färs, lök och vitlök i oljan i en stekpanna"
        )

        // Act
        val result = parseRecipeData(input)

        // Assert
        assertEquals(expected, result.instructions)
    }
}