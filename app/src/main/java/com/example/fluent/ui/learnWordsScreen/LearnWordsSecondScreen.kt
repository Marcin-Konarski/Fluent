package com.example.fluent.ui.learnWordsScreen

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
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.example.fluent.WordEventForScreen2
import com.example.fluent.WordEventForScreen4and5
import com.example.fluent.ui.components.AppDeleteButton
import com.example.fluent.ui.components.AppNavigationBar
import com.example.fluent.ui.components.AppTextField
import com.example.fluent.ui.components.AppTopBar

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


    Scaffold(
        topBar = {
            AppTopBar(
                title = "Learn Words",
                showBackButton = true,
            )
        },
        bottomBar = {
            AppNavigationBar(navController = navController)
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
                Text(text = "$correctWord", color = Color.Yellow)
            } else {
                Text(text = "")
            }
            Spacer(modifier = Modifier.height(2.dp))

            //Text(text = currentWord?.word ?: "No words available")
            Text(text = currentWord?.translation ?: "No words available")

            AppTextField(
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
                label = {
                    Text(text = "Word")
                },
                modifier = Modifier.fillMaxWidth()
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