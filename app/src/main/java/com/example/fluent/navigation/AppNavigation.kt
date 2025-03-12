package com.example.fluent.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.fluent.ui.screen1.Screen1
import com.example.fluent.ui.screen2.Screen2
import com.example.fluent.ui.screen3.Screen3

sealed class Screen(val route: String) {
    object Screen1 : Screen("screen1")
    object Screen2 : Screen("screen2/{itemId}") {
        fun createRoute(itemId: Int) = "screen2/$itemId"
    }
    object Screen3 : Screen("screen3")
}

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.Screen1.route) {
        composable(Screen.Screen1.route) {
            Screen1(
                onItemClick = { itemId ->
                    navController.navigate(Screen.Screen2.createRoute(itemId))
                },
                onButtonClick = {
                    navController.navigate(Screen.Screen3.route)
                }
            )
        }

        composable(
            route = Screen.Screen2.route,
            arguments = listOf(navArgument("itemId") { type = NavType.IntType })
        ) { backStackEntry ->
            val itemId = backStackEntry.arguments?.getInt("itemId") ?: -1 // (default value to avoid crashes)
            if (itemId == -1) {
                // Handle the error case (optional)
                return@composable
            }
            Screen2(
                itemId = itemId,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.Screen3.route) {
            Screen3(
                onButtonClick = {
                    navController.popBackStack() // Navigate back after adding a word
                }
            )
        }
    }
}