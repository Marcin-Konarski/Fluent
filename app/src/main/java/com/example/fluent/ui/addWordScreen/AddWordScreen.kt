package com.example.fluent.ui.addWordScreen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.fluent.WordEventForAddWord
import com.example.fluent.ui.components.AppTextField
import com.example.fluent.navigation.BlurredAppNavigationBar
import com.example.fluent.ui.components.ConfirmButton
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
    val isDropdownExpanded = remember { mutableStateOf(false) }

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


                // Category selection with dropdown
                ExposedDropdownMenuBox(
                    expanded = isDropdownExpanded.value,
                    onExpandedChange = { isDropdownExpanded.value = it },
                    modifier = Modifier.fillMaxWidth(0.9f)
                ) {
                    OutlinedTextField(
                        value = state.category,
                        onValueChange = {
                            viewModel.onEvent(WordEventForAddWord.SetCategory(it))
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        label = @Composable { Text("Category") },
                        trailingIcon = @Composable {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = isDropdownExpanded.value)
                        },
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                keyboardController?.hide()
                            }
                        )
                    )

                    // Show existing categories in dropdown
                    ExposedDropdownMenu(
                        expanded = isDropdownExpanded.value,
                        onDismissRequest = { isDropdownExpanded.value = false }
                    ) {
                        state.allCategories.forEach { category ->
                            DropdownMenuItem(
                                text = @Composable { Text(category) },
                                onClick = {
                                    viewModel.onEvent(WordEventForAddWord.SetCategory(category))
                                    isDropdownExpanded.value = false
                                }
                            )
                        }
                    }
                }


                Spacer(modifier = Modifier.height(48.dp))

                AppTextField(
                    value = state.word,
                    onValueChange = {
                        viewModel.onEvent(WordEventForAddWord.SetWordAddWord(it))
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    label = @Composable { Text(text = "Word") },
                    modifier = Modifier.fillMaxWidth(0.9f)
                )

                Spacer(modifier = Modifier.height(12.dp))

                AppTextField(
                    value = state.translation,
                    onValueChange = {
                        viewModel.onEvent(WordEventForAddWord.SetTranslation(it))
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    label = @Composable { Text(text = "Translation") },
                    modifier = Modifier.fillMaxWidth(0.9f)
                )

                Spacer(modifier = Modifier.height(72.dp))

                ConfirmButton(
                    onClick = {
                        onButtonClick()
                        viewModel.onEvent(WordEventForAddWord.SaveWordAddWord)
                    },
                    buttonText = "Save"
                )
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