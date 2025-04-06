package com.example.fluent.ui.addWordScreen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    var showCategoryManagementDialog by remember { mutableStateOf(false) } // Variable to show/hide 'Category management' dialog

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
                        onDeleteCategory = { category ->
                            categoriesViewModel.deleteCategory(category)
                        }
                    )
                }

                // Category Selection
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

                Spacer(modifier = Modifier.height(24.dp))

                // Column for the rest of the form fields (Word, Translation, and Save button)
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
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
                        label = @Composable { Text(text = "Word") },
                        modifier = Modifier.fillMaxWidth(0.8f)
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
                        label = @Composable { Text(text = "Translation") },
                        modifier = Modifier.fillMaxWidth(0.8f)
                    )

                    Spacer(modifier = Modifier.height(72.dp))

                    // Confirm Button to save the word
                    ConfirmButton(
                        onClick = {
                            // Ensure that the word, translation, and category are not empty
                            if (state.word.isNotBlank() && state.translation.isNotBlank() && state.category.isNotBlank()) {
                                // Save the word in the selected category
                                viewModel.onEvent(WordEventForAddWord.SaveWordAddWord)
                                onButtonClick()
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
