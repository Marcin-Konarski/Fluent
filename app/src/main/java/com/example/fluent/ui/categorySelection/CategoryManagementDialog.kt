package com.example.fluent.ui.categorySelection

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.fluent.data.Category
import com.example.fluent.ui.components.AppTextField
import com.example.fluent.ui.components.GlassyDialog
import com.example.fluent.ui.components.GlassyAlertDialog

@Composable
fun CategoryManagementDialog(
    categories: List<Category>,
    onDismiss: () -> Unit,
    onAddCategory: (String) -> Unit,
    onRenameCategory: (Category, String) -> Unit,
    categoriesViewModel: CategoriesViewModel = hiltViewModel(),
) {
    val scrollState = rememberScrollState()
    var newCategoryName by remember { mutableStateOf("") }
    var showNewCategoryField by remember { mutableStateOf(false) }
    val categoriesToDelete = remember { mutableStateListOf<Category>() }
    var showDeleteWarningFor by remember { mutableStateOf<Category?>(null) }

    val editableCategories = remember(categories) {
        categories.map { it to mutableStateOf(it.name) }.toMutableStateList()
    }

    // Using our new GlassyDialog for the main dialog
    GlassyDialog(
        onDismissRequest = onDismiss,
        content = {
            // Content layer
            Column(modifier = Modifier.padding(24.dp)) {
                // Title section with Add button
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        "Manage Categories",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    IconButton(onClick = {
                        newCategoryName = ""
                        showNewCategoryField = true
                    }) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add new category"
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Categories list
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(scrollState)
                        .padding(vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (categories.isEmpty() && !showNewCategoryField) {
                        Text(
                            text = "No categories yet",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    } else {
                        editableCategories.forEach { (category, nameState) ->
                            if (!categoriesToDelete.contains(category)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    AppTextField(
                                        value = nameState.value,
                                        onValueChange = { nameState.value = it },
                                        label = { Text("Category name") },
                                        modifier = Modifier.weight(1f)
                                    )
                                    IconButton(
                                        onClick = { showDeleteWarningFor = category },
                                        modifier = Modifier.padding(start = 8.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Delete ${category.name}",
                                            tint = Color.Red
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // New Category Input Field
                    if (showNewCategoryField) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AppTextField(
                                value = newCategoryName,
                                onValueChange = { newCategoryName = it },
                                label = { Text("Category name") },
                                modifier = Modifier.weight(1f)
                            )

                            IconButton(
                                onClick = {
                                    newCategoryName = ""
                                    showNewCategoryField = false
                                },
                                modifier = Modifier.padding(start = 4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Cancel adding category",
                                    tint = Color.Red
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Action buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    TextButton(
                        onClick = {
                            // Handle new category addition
                            if (showNewCategoryField && newCategoryName.isNotBlank()) {
                                onAddCategory(newCategoryName.trim())
                                newCategoryName = ""
                                showNewCategoryField = false
                            }

                            // Handle renaming
                            editableCategories.forEach { (category, nameState) ->
                                if (nameState.value.isNotBlank() && nameState.value != category.name) {
                                    onRenameCategory(category, nameState.value.trim())
                                }
                            }

                            // Handle deletions
                            categoriesToDelete.forEach { category ->
                                categoriesViewModel.deleteCategoryAndWords(category)
                            }

                            onDismiss()
                        }
                    ) {
                        Text("Done")
                    }
                }
            }
        },
        modifier = Modifier.fillMaxWidth(0.95f)
    )

    // Delete warning dialog using GlassyAlertDialog
    if (showDeleteWarningFor != null) {
        GlassyAlertDialog(
            onDismissRequest = { showDeleteWarningFor = null },
            title = "Delete Category",
            text = "This will also delete all words inside '${showDeleteWarningFor!!.name}'. Are you sure?",
            confirmButtonText = "Delete",
            onConfirm = {
                categoriesToDelete.add(showDeleteWarningFor!!)
                showDeleteWarningFor = null
            },
            dismissButtonText = "Cancel",
            onDismiss = { showDeleteWarningFor = null }
        )
    }
}