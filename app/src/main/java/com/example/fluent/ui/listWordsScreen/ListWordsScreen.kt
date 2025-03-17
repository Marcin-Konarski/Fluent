package com.example.fluent.ui.listWordsScreen

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.fluent.navigation.NavigationBar
import com.example.fluent.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Screen1(
    navController: NavHostController,
    viewModel: ListWordsViewModel = hiltViewModel(),
    onItemClick: (Int) -> Unit,
    onButtonClick: () -> Unit
) {
    val wordList = viewModel.wordList.collectAsState(initial = emptyList()).value
    val navigationList = listOf(
        NavigationBar("Screen1", Icons.Default.Apps),
        NavigationBar("Screen4", Icons.Default.Home),
        NavigationBar("Screen6", Icons.Default.Settings),
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

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { onButtonClick() }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
            }
        },
        bottomBar = {
            NavigationBar(
                containerColor = Color.Transparent,
                modifier = Modifier
                    .fillMaxWidth()
                    //.blur(radius = 2.dp)
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            items(wordList) { item ->
                ElevatedCard(
                    elevation = CardDefaults.elevatedCardElevation(
                        defaultElevation = 4.dp,
                        pressedElevation = 8.dp,
                        hoveredElevation = 10.dp,
                        focusedElevation = 6.dp
                    ),
                    shape = MaterialTheme.shapes.medium,
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
