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
import com.example.fluent.ui.components.FullScreenBlurredBackground
import com.example.fluent.navigation.BlurredAppNavigationBar
import com.example.fluent.ui.components.CategorySelection
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
    val selectedCategory = viewModel.selectedCategory.collectAsState().value

    Box(modifier = Modifier.fillMaxSize()) {
        FullScreenBlurredBackground(
            blurRadius = 5.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 8.dp)
            ) {
                // New Category selection component
                CategorySelection(
                    categories = categories,
                    selectedCategory = selectedCategory,
                    onCategorySelected = { category ->
                        viewModel.selectCategory(category)
                    },
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Word list (now in a separate Box with padding)
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
                                        text = if (selectedCategory.isEmpty())
                                            "No words added yet"
                                        else
                                            "No words in this category",
                                        style = MaterialTheme.typography.bodyLarge,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
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