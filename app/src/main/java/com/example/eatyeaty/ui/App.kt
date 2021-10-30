package com.example.eatyeaty.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


@Composable
fun App() {
    val controller = rememberNavController()

    NavHost(navController = controller, startDestination = "list") {

        composable("list") {
            ListScreen(onCreateClick = {})
        }
    }
}
