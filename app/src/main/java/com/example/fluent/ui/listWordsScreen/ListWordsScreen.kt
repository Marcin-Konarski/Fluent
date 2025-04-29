package com.example.fluent.ui.listWordsScreen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.fluent.ui.categorySelection.CategoriesViewModel
import com.example.fluent.ui.components.FullScreenBlurredBackground
import com.example.fluent.ui.components.GlossyAppCard


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListWordsScreen(
    navController: NavHostController,
    viewModel: ListWordsViewModel = hiltViewModel(),
    categoriesViewModel: CategoriesViewModel = hiltViewModel(),
    onItemClick: (Int) -> Unit,
) {
    val wordList = viewModel.wordList.collectAsState(initial = emptyList()).value
    val categories = viewModel.categories.collectAsState(initial = emptyList()).value
    val selectedCategoryId = viewModel.selectedCategoryId.collectAsState().value

    // Calculate the height of CategorySelection (adjust based on your UI)
    val categorySelectionHeight = 56.dp

    FullScreenBlurredBackground(
        navController = navController,
        showCategorySelection = true,
        categoriesViewModel = categoriesViewModel,
        categories = categories,
        selectedCategoryId = selectedCategoryId,
        onCategorySelected = { categoryId ->
            viewModel.selectCategory(categoryId)
        }
    ) {
        // Word list that will scroll underneath the CategorySelection
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            // Invisible spacer at the top of the screen with height of CategorySelection
            item {
                Spacer(modifier = Modifier.height(categorySelectionHeight + 20.dp))
            }

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

            item {
                Spacer(modifier = Modifier.height(85.dp))
            }
        }
    }
}