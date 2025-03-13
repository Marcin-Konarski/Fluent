package com.example.fluent.ui.screen3

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.fluent.WordEventForScreen3
import com.example.fluent.data.Word
import com.example.fluent.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Screen3(
    navController: NavHostController,
    viewModel: Screen3ViewModel = hiltViewModel(),
    onButtonClick: () -> Unit,
) {
    val state by viewModel.state.collectAsState()
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
        topBar = {
            TopAppBar(
                title = { Text("Add new word") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                onButtonClick()
                viewModel.onEvent(WordEventForScreen3.saveWord)
            }) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Save"
                )
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            TextField(
                value = state.word,
                onValueChange = { viewModel.onEvent(WordEventForScreen3.setWord(it)) },
                placeholder = {
                    Text(text = "word")
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = state.translation,
                onValueChange = { viewModel.onEvent(WordEventForScreen3.setTranslation(it)) },
                placeholder = {
                    Text(text = "translation")
                },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}