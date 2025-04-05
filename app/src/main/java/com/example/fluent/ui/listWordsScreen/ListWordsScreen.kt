package com.example.fluent.ui.listWordsScreen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.fluent.ui.components.FullScreenBlurredBackground
import com.example.fluent.navigation.BlurredAppNavigationBar
import com.example.fluent.ui.categorySelection.AddCategoryDialog
import com.example.fluent.ui.categorySelection.CategoriesViewModel
import com.example.fluent.ui.categorySelection.CategorySelection
import com.example.fluent.ui.components.ConfirmButton
import com.example.fluent.ui.components.GlossyAppCard


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListWordsScreen(
    navController: NavHostController,
    viewModel: ListWordsViewModel = hiltViewModel(),
    onItemClick: (Int) -> Unit,
    onButtonClick: () -> Unit
) {
    val wordList = viewModel.wordList.collectAsState(initial = emptyList()).value
    val categories = viewModel.categories.collectAsState(initial = emptyList()).value
    val selectedCategoryId = viewModel.selectedCategoryId.collectAsState().value
    val categoriesViewModel: CategoriesViewModel = hiltViewModel() // For managing categories
    var showAddCategoryDialog by remember { mutableStateOf(false) } // Variable to show/hide 'Add category' dialog
    val isCategoryEmpty = selectedCategoryId != null && // Conditions for showing the 'Delete Category' button
            selectedCategoryId > 0 &&
            wordList.none { it.categoryId == selectedCategoryId }

    Box(modifier = Modifier.fillMaxSize()) {
        FullScreenBlurredBackground(
            blurRadius = 5.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 8.dp)
            ) {
                // Display words category dialog
                if (showAddCategoryDialog) {
                    AddCategoryDialog(
                        onDismiss = { showAddCategoryDialog = false },
                        onConfirm = { newCategory ->
                            categoriesViewModel.addCategory(newCategory)
                            showAddCategoryDialog = false
                        }
                    )
                }

                // Display all categories in a row
                CategorySelection(
                    categories = categories.map { it.name },
                    selectedCategory = categories.find { it.id == selectedCategoryId }?.name.orEmpty(),
                    onCategorySelected = { selectedName ->
                        val selected = categories.find { it.name == selectedName }
                        viewModel.selectCategory(selected?.id)
                    },
                    onAddCategory = {
                        showAddCategoryDialog = true
                    }
                )

                // Word list
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                ) {
                    LazyColumn {
                        items(wordList) { item ->
                            GlossyAppCard(
                                word = item.word,
                                translation = item.translation,
                                onClick = { onItemClick(item.id) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(90.dp)
                                    .padding(vertical = 8.dp)
                            )
                        }

                        // Show message if no words in category
                        if (wordList.isEmpty()) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(32.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = if (categories.isEmpty())
                                            "No words added yet"
                                        else
                                            "No words in this category",
                                        style = MaterialTheme.typography.bodyLarge,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }

//                        // Show button to delete an empty category
//                        if (isCategoryEmpty) {
//                            item {
//                                ConfirmButton(
//                                    onClick = {
//                                        categories.find { it.id == selectedCategoryId }?.let { category ->
//                                            categoriesViewModel.deleteCategory(category)
//                                        }
//                                    },
//                                    buttonText = "Delete Category"
//                                )
//                            }
//                        }

                        item {
                            Spacer(modifier = Modifier.height(85.dp))
                        }
                    }
                }
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
