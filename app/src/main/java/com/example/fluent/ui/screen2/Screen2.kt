package com.example.fluent.ui.screen2

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.fluent.WordEventForScreen2
import com.example.fluent.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Screen2(
    navController: NavHostController,
    itemId: Int,
    viewModel: Screen2ViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    val item = viewModel.item.collectAsState().value
    val navigationList = listOf(
        com.example.fluent.navigation.NavigationBar("Screen1", Icons.Default.Apps),
        com.example.fluent.navigation.NavigationBar("Screen4", Icons.Default.Home),
        com.example.fluent.navigation.NavigationBar("Screen6", Icons.Default.Settings)
    )

    // Get the current route to dynamically update selected index
    val currentDestination = navController.currentBackStackEntry?.destination?.route
    val selectedIndex = when (currentDestination) {
        Screen.Screen1.route -> 0
        Screen.Screen4.route -> 1
        Screen.Screen5.route -> 1
        Screen.Screen6.route -> 2
        else -> 0
    }
    Text(text = "Item ID: $itemId")
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Go Back") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            onBackClick()
                            item?.let {
                                viewModel.onEvent(WordEventForScreen2.DeleteWord(it))
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete"
                        )
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = Color.Transparent,
                //modifier = Modifier.blur(radius = 16.dp)
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
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = item?.word ?: "",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.headlineLarge
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = item?.translation ?: "",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}