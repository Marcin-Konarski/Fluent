package com.example.fluent.ui.screen4

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.fluent.WordEventForScreen3
import com.example.fluent.navigation.Screen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Screen4(
    navController: NavHostController,
    viewModel: Screen4ViewModel = hiltViewModel()
) {
    val navigationList = listOf(
        com.example.fluent.navigation.NavigationBar("Screen1", Icons.Default.Apps),
        com.example.fluent.navigation.NavigationBar("Screen4", Icons.Default.Home),
        com.example.fluent.navigation.NavigationBar("Screen5", Icons.Default.Settings)
    )

    // Get the current route to dynamically update selected index
    val currentDestination = navController.currentBackStackEntry?.destination?.route
    val selectedIndex = when (currentDestination) {
        Screen.Screen1.route -> 0
        Screen.Screen4.route -> 1
        Screen.Screen5.route -> 2
        else -> 0
    }

    Scaffold(
        bottomBar = {
            NavigationBar {
                navigationList.forEachIndexed { index, navigation ->
                    NavigationBarItem(
                        selected = selectedIndex == index,
                        onClick = {
                            when (index) {
                                0 -> navController.navigate(Screen.Screen1.route)
                                1 -> navController.navigate(Screen.Screen4.route)
                                2 -> navController.navigate(Screen.Screen5.route)
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
    ){ paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ){
            Text(text = "SCREEN 4")
        }
    }
}