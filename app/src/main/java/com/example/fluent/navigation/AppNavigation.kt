package com.example.fluent.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.fluent.ui.listWordsScreen.ListWordsScreen
import com.example.fluent.ui.detailsScreen.DetailsScreen
import com.example.fluent.ui.addWordScreen.AddWordScreen
import com.example.fluent.ui.flashCardsScreen.FlashCardsScreen
import com.example.fluent.ui.learnWordsScreen.LearnWordsViewModel
import com.example.fluent.ui.learnWordsScreen.LearnWordsScreen
import com.example.fluent.ui.settingsScreen.SettingsScreen
import com.example.fluent.ui.theme.AppTheme

sealed class Screen(val route: String) {
    object Screen1 : Screen("screen1")  // FlashCards
    object Screen2 : Screen("screen2")  // ListWords (Main)
    object Screen3 : Screen("screen3")  // AddWord
    object Screen4 : Screen("screen4")  // LearnWords
    object Screen5 : Screen("screen5")  // Settings

    object Details : Screen("details/{itemId}") {
        fun createRoute(itemId: Int) = "details/$itemId" // Details
    }
}

@Composable
fun AppNavigation(
    navController: NavHostController,
    onThemeChange: (AppTheme) -> Unit
) {
    val learnWordsViewModel: LearnWordsViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = Screen.Screen2.route
    ) {
        composable(Screen.Screen1.route) {
            FlashCardsScreen(
                navController = navController
            )
        }

        composable(Screen.Screen2.route) {
            ListWordsScreen(
                navController = navController,
                onItemClick = { itemId ->
                    navController.navigate(Screen.Details.createRoute(itemId))
                },
            )
        }

        composable(Screen.Screen3.route) {
            AddWordScreen(
                navController = navController,
                onButtonClick = { navController.popBackStack() }
            )
        }

        composable(Screen.Screen4.route) {
            LearnWordsScreen(
                navController = navController,
                viewModel = learnWordsViewModel,
            )
        }

        composable(Screen.Screen5.route) {
            SettingsScreen(
                navController = navController,
                onThemeChange = onThemeChange
            )
        }

        composable(
            route = Screen.Details.route,
            arguments = listOf(navArgument("itemId") { type = NavType.IntType })
        ) { backStackEntry ->
            val itemId = backStackEntry.arguments?.getInt("itemId") ?: -1
            if (itemId == -1) return@composable

            DetailsScreen(
                navController = navController,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}