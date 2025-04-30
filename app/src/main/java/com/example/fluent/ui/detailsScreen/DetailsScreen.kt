package com.example.fluent.ui.detailsScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.fluent.WordEventForDeleteWord
import com.example.fluent.ui.components.AppTextField
import com.example.fluent.ui.components.FullScreenBlurredBackground
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.platform.LocalDensity
import com.example.fluent.ui.components.ConfirmButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    navController: NavHostController,
    viewModel: DetailsViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    val item = viewModel.item.collectAsState().value
    val categories = viewModel.categories.collectAsState().value
    var expanded by remember { mutableStateOf(false) }

    var wordText by remember { mutableStateOf("") }
    var translationText by remember { mutableStateOf("") }
    var selectedCategoryId by remember { mutableStateOf(0) }
    val selectedCategoryName = categories.firstOrNull { it.id == selectedCategoryId }?.name ?: "Select category"
    var textFieldSize by remember { mutableStateOf(Size.Zero) }
    val density = LocalDensity.current

    LaunchedEffect(item) {
        item?.let {
            wordText = it.word
            translationText = it.translation
            selectedCategoryId = it.categoryId
        }
    }

    FullScreenBlurredBackground(
        navController = navController,
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Top bar with back and delete buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween, // This pushes items to the edges
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left side with back button
                IconButton(onClick = onBackClick) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                }

                // Right side with delete button
                IconButton(
                    onClick = {
                        item?.let {
                            viewModel.onEvent(WordEventForDeleteWord.DeleteWord(it))
                            onBackClick()
                        }
                    }
                ) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = Color.White)
                }
            }

            // Content
            if (item != null) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = selectedCategoryName,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Category") },
                            trailingIcon = {
                                IconButton(onClick = { expanded = !expanded }) {
                                    Icon(Icons.Filled.ArrowDropDown, contentDescription = "Dropdown")
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .onGloballyPositioned { coordinates ->
                                    textFieldSize = coordinates.size.toSize()
                                }
                                .clickable { expanded = !expanded }
                        )

                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            modifier = Modifier
                                .width(with(density) { textFieldSize.width.toDp() }) // Match width
                                .background(Color.Black.copy(alpha = 0.5f))
                        ) {
                            categories.forEach { category ->
                                DropdownMenuItem(
                                    text = { Text(category.name) },
                                    onClick = {
                                        selectedCategoryId = category.id
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    AppTextField(
                        value = wordText,
                        onValueChange = { wordText = it },
                        label = { Text("Word") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    AppTextField(
                        value = translationText,
                        onValueChange = { translationText = it },
                        label = { Text("Translation") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(70.dp))

                    ConfirmButton(
                        onClick = {
                            viewModel.editWord(wordText, translationText, selectedCategoryId)
                            onBackClick()
                        },
                        buttonText = "Save"
                    )
                }
            }
        }
    }
}