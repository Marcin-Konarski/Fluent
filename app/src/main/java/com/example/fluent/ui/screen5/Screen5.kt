package com.example.fluent.ui.screen5

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.fluent.navigation.Screen
import com.example.fluent.ui.screen4.SharedViewModel
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.example.fluent.WordEventForScreen4and5

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Screen5(
    navController: NavHostController,
    state: Int,
    viewModel: SharedViewModel = hiltViewModel(),
    onNavigateToScreen4: () -> Unit
) {
    val currentWord by viewModel.currentWord.collectAsState() // Observe current word
    val userInput by viewModel.userInput.collectAsState()
    val correctWord by viewModel.correctWord.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current

    val navigationList = listOf(
        com.example.fluent.navigation.NavigationBar("Screen1", Icons.Default.Apps),
        com.example.fluent.navigation.NavigationBar("Screen5", Icons.Default.Home),
        com.example.fluent.navigation.NavigationBar("Screen6", Icons.Default.Settings)
    )

    // Get the current route to dynamically update selected index
    val currentDestination = navController.currentBackStackEntry?.destination?.route
    val selectedIndex = when (currentDestination) {
        Screen.Screen1.route -> 0
        Screen.Screen5.route -> 1
        Screen.Screen6.route -> 2
        else -> 0
    }

    Scaffold(
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
                                // Route commented out to avoid bug with the button.
                                // This route is not needed anyways thus maybe I'll
                                // delete routes to current screen from all screens.
                                0 -> navController.navigate(Screen.Screen1.route)
                                //1 -> navController.navigate(Screen.Screen5.route)
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
    ){ paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if (correctWord != null) {
                Text(text = "Correct Word: $correctWord", color = Color.Red)
            } else {
                Text(text = "")
            }
            Spacer(modifier = Modifier.height(4.dp))

            //Text(text = currentWord?.word ?: "No words available")
            Text(text = currentWord?.translation ?: "No translation available")

            OutlinedTextField(
                value = userInput,
                onValueChange = {
                    viewModel.onEvent(WordEventForScreen4and5.SetWordInput(it))
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                        navController.popBackStack()
                    }
                ),
//                textStyle = TextStyle(
//                    Color(color = DeepBlue)
//                ),
                label = {
                    Text(text = "Word")
                },
                modifier = Modifier
                    .fillMaxWidth()
            )

            Button(
                onClick = {
                    viewModel.onEvent(WordEventForScreen4and5.CheckAnswer)
                    onNavigateToScreen4()
                }
            ) {
                Text(text = "Back to Screen 4. State: $state")
            }
        }
    }
}