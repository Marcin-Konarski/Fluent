package com.example.fluent.ui.addWordScreen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.fluent.WordEventForAddWord
import com.example.fluent.ui.components.AppTextField
import com.example.fluent.navigation.BlurredAppNavigationBar
import com.example.fluent.ui.categorySelection.CategoryManagementDialog
import com.example.fluent.ui.categorySelection.CategoriesViewModel
import com.example.fluent.ui.components.ConfirmButton
import com.example.fluent.ui.components.FullScreenBlurredBackground
import com.example.fluent.ui.categorySelection.CategorySelection




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddWordScreen(
    navController: NavHostController,
    viewModel: AddWordViewModel = hiltViewModel(),
    categoriesViewModel: CategoriesViewModel = hiltViewModel(),
    onButtonClick: () -> Unit,
) {
    val state by viewModel.state.collectAsState()
    val categoryNames = state.allCategories.map { it.name }
    val uiState by categoriesViewModel.uiState.collectAsState()
    var showCategoryManagementDialog by remember { mutableStateOf(false) }

    // Create focus requesters for the text fields
    val wordFieldFocusRequester = remember { FocusRequester() }
    val translationFieldFocusRequester = remember { FocusRequester() }

    // Track previous word and translation to detect changes
    val previousWord = remember { mutableStateOf("") }
    val previousTranslation = remember { mutableStateOf("") }

    // Check if a word was just saved by detecting when fields are cleared
    LaunchedEffect(state.word, state.translation) {
        if (previousWord.value.isNotBlank() && previousTranslation.value.isNotBlank() &&
            state.word.isBlank() && state.translation.isBlank()) {
            // Word was just saved (fields were reset)
            wordFieldFocusRequester.requestFocus()
        }

        // Update previous values
        previousWord.value = state.word
        previousTranslation.value = state.translation
    }

    FullScreenBlurredBackground(
        blurRadius = 5.dp
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 8.dp)
            ) {

                // Display words category dialog
                if (showCategoryManagementDialog) {
                    CategoryManagementDialog(
                        categories = uiState.categories,
                        onDismiss = { showCategoryManagementDialog = false },
                        onAddCategory = { newCategoryName ->
                            categoriesViewModel.addCategory(newCategoryName)
                        },
                        onRenameCategory = { category, newName ->
                            categoriesViewModel.renameCategory(category, newName)
                        },
                        categoriesViewModel = categoriesViewModel
                    )
                }

                CategorySelection(
                    categories = categoryNames,
                    selectedCategory = state.category,
                    onCategorySelected = { category ->
                        viewModel.onEvent(WordEventForAddWord.SetCategory(category))
                    },
                    onAddCategory = {
                        showCategoryManagementDialog = true
                    },
                    showAllOption = true
                ) {}

                Spacer(modifier = Modifier.height(100.dp))

                // Column for the form fields (Word, Translation, and Save button)
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top,
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                        .padding(top = 16.dp)
                ) {
                    // Word input field
                    AppTextField(
                        value = state.word,
                        onValueChange = {
                            viewModel.onEvent(WordEventForAddWord.SetWordAddWord(it))
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = {
                                translationFieldFocusRequester.requestFocus()
                            }
                        ),
                        label = @Composable { Text(text = "Word") },
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .focusRequester(wordFieldFocusRequester)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Translation input field
                    AppTextField(
                        value = state.translation,
                        onValueChange = {
                            viewModel.onEvent(WordEventForAddWord.SetTranslation(it))
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                if (state.word.isNotBlank() && state.translation.isNotBlank() && state.category.isNotBlank()) {
                                    viewModel.onEvent(WordEventForAddWord.SaveWordAddWord)
                                }
                            }
                        ),
                        label = @Composable { Text(text = "Translation") },
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .focusRequester(translationFieldFocusRequester)
                    )

                    Spacer(modifier = Modifier.height(72.dp))

                    // Confirm Button to save the word
                    ConfirmButton(
                        onClick = {
                            if (state.word.isNotBlank() && state.translation.isNotBlank() && state.category.isNotBlank()) {
                                viewModel.onEvent(WordEventForAddWord.SaveWordAddWord)
                            }
                        },
                        buttonText = "Save"
                    )
                }
            }

            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
            ) {
                BlurredAppNavigationBar(navController = navController)
            }
        }
    }
}