package com.example.fluent.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.fluent.ui.listWordsScreen.ListWordsScreen
import com.example.fluent.ui.detailsScreen.DetailsScreen
import com.example.fluent.ui.addWordScreen.AddWordScreen
import com.example.fluent.ui.learnWordsScreen.LearnWordsScreen2
import com.example.fluent.ui.learnWordsScreen.SharedViewModel
import com.example.fluent.ui.learnWordsScreen.LearnWordsScreen1
import com.example.fluent.ui.flashCardsScreen.FlashCardsScreen


sealed class Screen(val route: String) {
    object Screen1: Screen("screen1")
    object Screen2: Screen("screen2/{itemId}") {
        fun createRoute(itemId: Int) = "screen2/$itemId"
    }
    object Screen3: Screen("screen3")
    object Screen4: Screen("screen4")
    object Screen5: Screen("Screen5")
    object Screen6: Screen("Screen6")

    object YourScreen: Screen("YourScreen")
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

        // Creates a graph of 2 screens in order to share one viewModel between 2 screens
        navigation(
            startDestination = Screen.Screen4.route,
            route = "screen4_graph"
        ) {


            // Finds the parent navigation graph's back stack entry.
            // Instead of scoping the ViewModel to Screen4 or Screen5
            // it is scoped to screen4_graph, making it persist across
            // both screens. Thus the same ViewModel instance is used
            // on both screens. Creates the Screen4 and passes this viewModel
            // instance as well as the state. The same for the second composable.
            composable(Screen.Screen4.route) { entry ->
                val parentEntry = remember(entry) {
                    navController.getBackStackEntry("screen4_graph")
                }
//                val viewModel: SharedViewModel = hiltViewModel(parentEntry)
                val state = sharedViewModel.sharedState.collectAsStateWithLifecycle().value

                LearnWordsScreen2(
                    navController = navController,
                    state = state,
                    viewModel = sharedViewModel,
                    onNavigateToScreen5 = {
                        sharedViewModel.updateState()
                        navController.navigate(Screen.Screen5.route)
                    }
                )
            }

            composable(Screen.Screen5.route) { entry ->
                val parentEntry = remember(entry) {
                    navController.getBackStackEntry("screen4_graph")
                }
//                val viewModel: SharedViewModel = hiltViewModel(parentEntry)
                val state = sharedViewModel.sharedState.collectAsStateWithLifecycle().value

                LearnWordsScreen1(
                    navController = navController,
                    state = state,
                    viewModel = sharedViewModel,
                    onNavigateToScreen4 = {
                        sharedViewModel.updateState()
                        navController.popBackStack()
                    }
                )
            }
        }

        composable(Screen.Screen6.route) {
            FlashCardsScreen(navController = navController)
        }
    }
}
