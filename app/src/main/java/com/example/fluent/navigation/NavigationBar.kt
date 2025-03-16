package com.example.fluent.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController


data class NavigationBar (
    val label: String,
    val icon: ImageVector,
)

@Composable
fun ContentScreen(
    modifier: androidx.compose.ui.Modifier,
    navController: NavHostController,
    selectedIndex: Int
){
    when(selectedIndex){
        0 -> navController.navigate(Screen.Screen1.route)
        1 -> navController.navigate(Screen.Screen4.route)
        2 -> navController.navigate(Screen.Screen6.route)
    }
}