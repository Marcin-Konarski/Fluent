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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.fluent.WordEventForScreen3
import com.example.fluent.ui.components.AppTextField
import com.example.fluent.ui.components.AppTopBar
import com.example.fluent.ui.components.BlurredAppNavigationBar
import dev.chrisbanes.haze.HazeState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddWordScreen(
    navController: NavHostController,
    viewModel: AddWordViewModel = hiltViewModel(),
    onButtonClick: () -> Unit,
) {
    val state by viewModel.state.collectAsState()

    // Requesters to control focus (jumping from one text field to the second text filed)
    val wordFocusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val hazeState = remember {
        HazeState()
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Add new word",
                modifier = Modifier,
                showBackButton = true,
//                onBackClick =
//                actions =
                )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                onButtonClick()
                viewModel.onEvent(WordEventForScreen3.SaveWord)
            }) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Save"
                )
            }
        },
        bottomBar = {
            BlurredAppNavigationBar(navController = navController, hazeState = hazeState)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {

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
                label = {
                    Text(text = "Word")
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

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
                label = {
                    Text(text = "Translation")
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}