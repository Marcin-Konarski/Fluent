package com.example.fluent.ui.learnWordsScreen

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.fluent.WordEventForScreen4and5
import com.example.fluent.ui.components.AppNavigationBar
import com.example.fluent.ui.components.AppTextField
import com.example.fluent.ui.components.AppTopBar
import com.example.fluent.ui.theme.DeepMagenta

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Screen4(
    navController: NavHostController,
    state: Int,
    viewModel: SharedViewModel = hiltViewModel(),
    onNavigateToScreen5: () -> Unit
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
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .imePadding(), //dodajemy padding uwzględniający klawiaturę
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (correctWord != null) {
                Text(
                    text = "$correctWord",
                    color = Color.Yellow,
                    modifier = Modifier.offset(y = (-20).dp) // Przesuwamy tekst o 20 dp w górę
                )
            } else {
                Text(text = "")
            }
            Spacer(modifier = Modifier.height(2.dp))

            //Text(text = currentWord?.word ?: "No words available")
            Text(
                text = currentWord?.translation ?: "No words available",
                modifier = Modifier.offset(y = (-20).dp) // Przesuwamy tekst o 20 dp w górę
            )

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
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .offset(y = (-20).dp) // Przesuwamy pole tekstowe o 20 dp w górę
            )
            //do animacji przycisku - zapisywanie stanow wcisniecia przycisku
            val interactionSource = remember { MutableInteractionSource() }
            val isPressed by interactionSource.collectIsPressedAsState()
            val scale = animateFloatAsState(if (isPressed) 0.95f else 1f, label = "")
            var clicked by remember { mutableStateOf(false) }

            LaunchedEffect(key1 = clicked) {
                if (clicked) {
                    onNavigateToScreen5()
                    clicked = false
                }
            }

            Button(
                onClick = {
                    viewModel.onEvent(WordEventForScreen4and5.CheckAnswer)
                    clicked = true
                },
                modifier = Modifier
                    .padding(top = 5.dp) //odstęp
                    .height(45.dp) //wysokosc przycisku
                    .width(320.dp) //szerokosc przycisku
                    .scale(scale.value)
                    .offset(y = (-20).dp), // Przesuwamy przycisk o 20 dp w górę
                interactionSource = interactionSource,
                colors = ButtonDefaults.buttonColors(
                    containerColor = DeepMagenta, //tlo
                    contentColor = Color.White //napis na buttonie
                ),
                shape = RoundedCornerShape(16.dp), //zaokraglone rogi
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 8.dp, //dodajemy cień
                    pressedElevation = 12.dp,
                    focusedElevation = 12.dp
                )
            ) {
                Text(text = "Confirm")
            }
        }
    }
}