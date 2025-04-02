package com.example.fluent.ui.learnWordsScreen

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.fluent.R
import com.example.fluent.WordEventForScreen4and5
import com.example.fluent.navigation.BlurredAppNavigationBar
import com.example.fluent.ui.components.AppTextField
import com.example.fluent.ui.components.FullScreenBlurredBackground
import com.example.fluent.ui.components.ProgressBar
import com.example.fluent.ui.theme.DeepMagenta
import dev.chrisbanes.haze.HazeState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LearnWordsScreen2(
    navController: NavHostController,
    state: Int,
    viewModel: SharedViewModel = hiltViewModel(),
    onNavigateToScreen5: () -> Unit
) {
    val currentWord by viewModel.currentWord.collectAsState()
    val userInput by viewModel.userInput.collectAsState()
    val correctWord by viewModel.correctWord.collectAsState()
    val progress by viewModel.progress.collectAsState()
    val learnedWords by viewModel.learnedWords.collectAsState()
    val leftWords by viewModel.leftWords.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val hazeState = remember { HazeState() }

    Scaffold(
        bottomBar = {
            BlurredAppNavigationBar(navController = navController, hazeState = hazeState)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .imePadding(),
            verticalArrangement = Arrangement.Top, // Keeps content starting at the top
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Progress Bar at the Top
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Your Progress", modifier = Modifier.padding(bottom = 4.dp))
                ProgressBar(progress = progress)
                Row(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
                    Text(text = "Learned: $learnedWords")
                    Spacer(modifier = Modifier.weight(1f))
                    Text(text = "Left: $leftWords")
                }
            }

            // Spacer to push testing area to the center
            Spacer(modifier = Modifier.weight(1f))

            // Testing Area (Centered)
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                if (correctWord != null) {
                    Text(text = "$correctWord", color = Color.Yellow)
                }

                Text(text = currentWord?.translation ?: "No words available")

                AppTextField(
                    value = userInput,
                    onValueChange = { viewModel.onEvent(WordEventForScreen4and5.SetWordInput(it)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
                    label = { Text(text = "Word") },
                    modifier = Modifier.fillMaxWidth().padding(20.dp)
                )

                Button(
                    onClick = { viewModel.onEvent(WordEventForScreen4and5.CheckAnswer); onNavigateToScreen5() },
                    modifier = Modifier.padding(top = 5.dp).height(45.dp).width(320.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = DeepMagenta, contentColor = Color.White),
                    shape = RoundedCornerShape(16.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp, pressedElevation = 12.dp, focusedElevation = 12.dp)
                ) {
                    Text(text = "Confirm")
                }
            }
        }
    }
}
