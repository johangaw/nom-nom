package com.example.nomnom.ui

import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavBackStackEntry
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.nomnom.data.RecipeListViewModel
import com.example.nomnom.data.EditRecipeViewModel
import com.example.nomnom.data.Recipe
import com.example.nomnom.data.asRecipe
import com.example.nomnom.repositories.fetchAndStore
import com.example.nomnom.repositories.loadData
import com.example.nomnom.services.throwOnCancellation
import com.example.nomnom.ui.screen.CreateRecipeScreen
import com.example.nomnom.ui.screen.EditRecipeScreen
import com.example.nomnom.ui.screen.ShowRecipeScreen
import com.example.nomnom.ui.theme.NomNomTheme
import kotlinx.coroutines.launch
import java.net.URLDecoder
import java.net.URLEncoder

sealed class Route(val route: String) {
    object List : Route("list")

    object Create : Route("create?url={url}") {
        val arguments = listOf(navArgument("url") { defaultValue = "" })
        fun link(url: String = ""): String {
            return this.route.replace("{url}", URLEncoder.encode(url, "utf-8"))
        }

        fun parseUrl(backStackEntry: NavBackStackEntry): String {
            return backStackEntry.arguments?.getString("url")
                ?.let { URLDecoder.decode(it, "utf-8") } ?: ""
        }
    }

    object Show : Route("show/{id}") {
        fun link(recipe: Recipe): String {
            return this.route.replace("{id}", recipe.id.toString())
        }

        fun parseId(backStackEntry: NavBackStackEntry): String {
            return backStackEntry.arguments?.getString("id")!!
        }
    }

    object Edit : Route("edit/{id}") {
        fun link(recipe: Recipe): String {
            return this.route.replace("{id}", recipe.id.toString())
        }

        fun parseId(backStackEntry: NavBackStackEntry): String {
            return backStackEntry.arguments?.getString("id")!!
        }
    }
}


@Composable
fun App(
    openUrl: (url: String) -> Unit,
    recipeListModel: RecipeListViewModel,
    editModel: EditRecipeViewModel,
) {
    val controller = rememberNavController()

    NomNomTheme {
        NavHost(navController = controller, startDestination = Route.List.route) {

            composable(Route.List.route) {
                val recipes by recipeListModel.recipes.collectAsState(listOf())

                var showDialog by remember { mutableStateOf(false) }
                ListScreen(
                    recipes = recipes,
                    onCreateClick = {
                        showDialog = true
                    },
                    onRecipeEdit = {
                        controller.navigate(Route.Edit.link(it))
                    },
                    onRecipeSelect = {
                        controller.navigate(Route.Show.link(it))
                    })

                if (showDialog)
                    RecipeUrlDialog(
                        onDismissRequest = { showDialog = false },
                        onSuccess = {
                            controller.navigate(Route.Create.link(it))
                        }
                    )
            }

            composable(Route.Create.route, Route.Create.arguments) { backStackEntry ->
                val recipe by editModel.recipe.observeAsState(Recipe())
                var loading by remember { mutableStateOf(true) }
                val recipeUrl = Route.Create.parseUrl(backStackEntry)
                val context = LocalContext.current
                val scope = rememberCoroutineScope()

                LaunchedEffect(recipeUrl) {
                    loading = true
                    val recipeDAO = loadData(recipeUrl)
                    val imageUri =
                        runCatching { fetchAndStore(context)(recipeDAO.imageUrl) }
                            .throwOnCancellation()
                            .map { it.toString() }
                            .getOrDefault("")
                    editModel.updateRecipe(recipeDAO.asRecipe(imageUri))
                    loading = false
                }

                CreateRecipeScreen(
                    loading = loading,
                    recipe = recipe,
                    onRecipeChange = editModel::updateRecipe,
                    requestGalleryImage = editModel::requestGalleryImage,
                    requestCameraImage = editModel::requestCameraImage,
                    onCreateClick = {
                        scope.launch {
                            loading = true
                            val newRecipe = editModel.createRecipe(recipe)
                            controller.navigate(Route.Show.link(newRecipe)) {
                                popUpTo(Route.List.route)
                            }
                        }
                    }
                )
            }

            composable(Route.Show.route) { backStackEntry ->
                val recipe by editModel.recipe.observeAsState(Recipe())
                var loading by remember { mutableStateOf(true) }
                val recipeId = Route.Show.parseId(backStackEntry)
                LaunchedEffect(recipeId) {
                    loading = true
                    editModel.selectRecipe(recipeId)
                    loading = false
                }

                ShowRecipeScreen(
                    loading = loading,
                    recipe = recipe,
                    openUrl = openUrl
                )
            }

            composable(Route.Edit.route) { backStackEntry ->
                val recipe by editModel.recipe.observeAsState(Recipe())
                var loading by remember { mutableStateOf(true) }
                val recipeId = Route.Edit.parseId(backStackEntry)
                LaunchedEffect(recipeId) {
                    loading = true
                    editModel.selectRecipe(recipeId)
                    loading = false
                }

                EditRecipeScreen(
                    recipe = recipe,
                    onRecipeChange = {
                        editModel.updateRecipe(it)
                    },
                    loading = loading,
                    requestGalleryImage = editModel::requestGalleryImage,
                    requestCameraImage = editModel::requestCameraImage,
                )
            }
        }
    }
}

