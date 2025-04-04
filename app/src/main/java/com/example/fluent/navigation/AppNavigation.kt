package com.example.fluent.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.fluent.ui.listWordsScreen.ListWordsScreen
import com.example.fluent.ui.detailsScreen.DetailsScreen
import com.example.fluent.ui.addWordScreen.AddWordScreen
import com.example.fluent.ui.learnWordsScreen.SharedViewModel
import com.example.fluent.ui.flashCardsScreen.FlashCardsScreen
import com.example.fluent.ui.learnWordsScreen.LearnWordsScreen


sealed class Screen(val route: String) {
    object Screen1: Screen("screen1")
    object Screen3: Screen("screen3")
    object Screen4: Screen("screen4")
    object Screen5: Screen("screen5/{itemId}") {
        fun createRoute(itemId: Int) = "screen5/$itemId"
    }
    object Screen6: Screen("Screen6")
}

@Composable
fun AppNavigation(navController: NavHostController) {
    val navController = rememberNavController()
    val sharedViewModel: SharedViewModel = hiltViewModel()
    NavHost(
        navController = navController,
        startDestination = Screen.Screen1.route
    ) {

        composable(Screen.Screen1.route) {
            ListWordsScreen(
                navController = navController,
                onItemClick = { itemId ->
                    navController.navigate(Screen.Screen5.createRoute(itemId))
                },
                onButtonClick = {
                    navController.navigate(Screen.Screen3.route)
                }
            )
        }

        composable(
            route = Screen.Screen5.route,
            arguments = listOf(navArgument("itemId") { type = NavType.IntType })
        ) { backStackEntry ->
            val itemId = backStackEntry.arguments?.getInt("itemId") ?: -1
            if (itemId == -1) return@composable

            DetailsScreen(
                navController = navController,
                itemId = itemId,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.Screen3.route) {
            AddWordScreen(
                navController = navController,
                onButtonClick = { navController.popBackStack() }
            )
        }

        composable(Screen.Screen4.route) { entry ->
            LearnWordsScreen(
                navController = navController,
                viewModel = sharedViewModel,
            )
        }

        composable(Screen.Screen6.route) {
            FlashCardsScreen(navController = navController)
        }
    }
}
