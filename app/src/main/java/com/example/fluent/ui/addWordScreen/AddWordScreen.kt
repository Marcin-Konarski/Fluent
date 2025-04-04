package com.example.fluent.ui.addWordScreen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.fluent.WordEventForScreen3
import com.example.fluent.ui.components.AppTextField
import com.example.fluent.navigation.BlurredAppNavigationBar
import com.example.fluent.ui.components.FullScreenBlurredBackground


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddWordScreen(
    navController: NavHostController,
    viewModel: AddWordViewModel = hiltViewModel(),
    onButtonClick: () -> Unit,
) {
    val state by viewModel.state.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current

    FullScreenBlurredBackground(
        blurRadius = 5.dp
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(modifier = Modifier.height(32.dp))

                AppTextField(
                    value = state.word,
                    onValueChange = {
                        viewModel.onEvent(WordEventForScreen3.SetWord(it))
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
                    label = { Text(text = "Word") },
                    modifier = Modifier.fillMaxWidth(0.9f) // Make text fields a bit narrower
                )

                Spacer(modifier = Modifier.height(12.dp))

                AppTextField(
                    value = state.translation,
                    onValueChange = {
                        viewModel.onEvent(WordEventForScreen3.SetTranslation(it))
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            keyboardController?.hide()
                            onButtonClick()
                            viewModel.onEvent(WordEventForScreen3.SaveWord)
                        }
                    ),
                    label = { Text(text = "Translation") },
                    modifier = Modifier.fillMaxWidth(0.9f)
                )

                Spacer(modifier = Modifier.height(24.dp)) // More space before button

                FloatingActionButton(
                    onClick = {
                        onButtonClick()
                        viewModel.onEvent(WordEventForScreen3.SaveWord)
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Save"
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
            ) {
                BlurredAppNavigationBar(navController = navController)
            }
        }
    }
}
