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
import com.example.fluent.ui.categorySelection.CategoriesViewModel
import com.example.fluent.ui.components.ConfirmButton
import com.example.fluent.ui.components.FullScreenBlurredBackground

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
        navController = navController,
        showCategorySelection = true,
        categoriesViewModel = categoriesViewModel,
        categories = state.allCategories,
        selectedCategoryId = state.allCategories.find { it.name == state.category }?.id,
        onCategorySelected = { categoryId ->
            val categoryName = state.allCategories.find { it.id == categoryId }?.name ?: ""
            viewModel.onEvent(WordEventForAddWord.SetCategory(categoryName))
        },
        showAllCategoryOption = false
    ) {
        // Column for the form fields (Word, Translation, and Save button)
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 240.dp)
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
}