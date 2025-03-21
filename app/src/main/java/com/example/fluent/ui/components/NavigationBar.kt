package com.example.fluent.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import com.example.fluent.navigation.NavigationBar as NavBarItem
import com.example.fluent.navigation.Screen


@Composable
fun AppNavigationBar(
    navController: NavHostController,
    modifier: Modifier = Modifier
){
    val navigationList = listOf(
        NavBarItem("Screen1", Icons.Default.Apps),
        NavBarItem("Screen4", Icons.Default.Home),
        NavBarItem("Screen6", Icons.Default.Settings)
    )

    val currentDestination = navController.currentBackStackEntry?.destination?.route
    val selectedIndex = when (currentDestination) {
        Screen.Screen1.route -> 0
        Screen.Screen4.route -> 1
        Screen.Screen5.route -> 1
        Screen.Screen6.route -> 2
        else -> 0
    }

    NavigationBar(
        containerColor = Color.Transparent,
        modifier = modifier
    ) {
        navigationList.forEachIndexed { index, navigation ->
            NavigationBarItem(
                selected = selectedIndex == index,
                onClick = {
                    when (index) {
                        0 -> navController.navigate(Screen.Screen1.route)
                        1 -> navController.navigate(Screen.Screen4.route)
                        2 -> navController.navigate(Screen.Screen6.route)
                    }
                },
                icon = {
                    Icon(imageVector = navigation.icon, contentDescription = "Icon")
                },
                label = {
                    Text(text = navigation.label)
                }
            )
        }
    }
}