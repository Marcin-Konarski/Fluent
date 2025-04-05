package com.example.fluent.ui.listWordsScreen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.fluent.ui.components.FullScreenBlurredBackground
import com.example.fluent.navigation.BlurredAppNavigationBar
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
    val isDropdownExpanded = remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        FullScreenBlurredBackground(
            blurRadius = 5.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                // Category dropdown selector
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    ExposedDropdownMenuBox(
                        expanded = isDropdownExpanded.value,
                        onExpandedChange = { isDropdownExpanded.value = it },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = selectedCategory.ifEmpty { "All Categories" },
                            onValueChange = { },
                            readOnly = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            label = @Composable { Text("Category Filter") },
                            trailingIcon = @Composable {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = isDropdownExpanded.value)
                            }
                        )

                        ExposedDropdownMenu(
                            expanded = isDropdownExpanded.value,
                            onDismissRequest = { isDropdownExpanded.value = false }
                        ) {
                            // "All" option
                            DropdownMenuItem(
                                text = @Composable { Text("All Categories") },
                                onClick = {
                                    viewModel.selectCategory("")
                                    isDropdownExpanded.value = false
                                }
                            )

                            // Individual categories
                            categories.forEach { category ->
                                DropdownMenuItem(
                                    text = @Composable { Text(category) },
                                    onClick = {
                                        viewModel.selectCategory(category)
                                        isDropdownExpanded.value = false
                                    }
                                )
                            }
                        }
                    }
                }

                // Word list
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
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

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        ) {
            BlurredAppNavigationBar(navController = navController)
        }
    }
}