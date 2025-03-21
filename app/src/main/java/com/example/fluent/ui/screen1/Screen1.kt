package com.example.fluent.ui.screen1

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.fluent.navigation.ContentScreen
import com.example.fluent.navigation.NavigationBar
import com.example.fluent.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Screen1(
    navController: NavHostController,
    viewModel: Screen1ViewModel = hiltViewModel(),
    onItemClick: (Int) -> Unit,
    onButtonClick: () -> Unit
) {
    val items = viewModel.items.collectAsState(initial = emptyList()).value
    val navigationList = listOf(
        NavigationBar("Screen1", Icons.Default.Apps),
        NavigationBar("Screen4", Icons.Default.Home),
        NavigationBar("Screen5", Icons.Default.Settings),
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
        topBar = {
            TopAppBar(
                title = { Text("Words List") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { onButtonClick() }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
            }
        },
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
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            items(items) { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .clickable { onItemClick(item.id) }
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = item.word, style = MaterialTheme.typography.headlineSmall)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = item.translation)
                    }
                }
            }
        }
    }
}
