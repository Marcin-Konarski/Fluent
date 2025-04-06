package com.example.fluent.ui.categorySelection

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
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
import com.example.fluent.data.Category
import com.example.fluent.ui.components.AppTextField

@Composable
fun CategoryManagementDialog(
    categories: List<Category>,
    onDismiss: () -> Unit,
    onAddCategory: (String) -> Unit,
    onRenameCategory: (Category, String) -> Unit,
    onDeleteCategory: (Category) -> Unit
) {
    val scrollState = rememberScrollState()
    var newCategoryName by remember { mutableStateOf("") }
    var showNewCategoryField by remember { mutableStateOf(false) }
    val editableCategories = remember(categories) {
        categories.map { it to mutableStateOf(it.name) }
    }

    // Track categories marked for deletion
    var categoriesToDelete by remember { mutableStateOf(setOf<Category>()) }

    // Track the list of visible categories after deletion
    var visibleCategories by remember { mutableStateOf(categories) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Manage Categories")
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
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
                    .padding(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // List of existing categories with rename and delete functionality
                if (visibleCategories.isEmpty() && !showNewCategoryField) {
                    Text(
                        text = "No categories yet",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                } else {
                    visibleCategories.forEach { category ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val nameState = editableCategories.firstOrNull { it.first == category }?.second

                            nameState?.let {
                                AppTextField(
                                    value = it.value,
                                    onValueChange = { it },
                                    label = { Text("Category name") },
                                    modifier = Modifier.weight(1f)
                                )
                            }

                            // Bin icon to mark category for deletion
                            IconButton(
                                onClick = {
                                    // Mark the category for deletion and remove it from visible list
                                    categoriesToDelete = categoriesToDelete.toMutableSet().apply {
                                        if (contains(category)) remove(category) else add(category)
                                    }
                                    visibleCategories = visibleCategories.filterNot { it == category }
                                },
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

                // New Category Input
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
                            onClick = { showNewCategoryField = false },
                            modifier = Modifier.padding(start = 8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Cancel adding category",
                                tint = Color.Red
                            )
                        }

//                        IconButton(
//                            onClick = {
//                                if (newCategoryName.isNotBlank()) {
//                                    onAddCategory(newCategoryName.trim())
//                                    newCategoryName = ""
//                                    showNewCategoryField = false
//                                }
//                            },
//                            enabled = newCategoryName.isNotBlank(),
//                            modifier = Modifier.padding(start = 4.dp)
//                        ) {
//                            Icon(
//                                imageVector = Icons.Default.Check,
//                                contentDescription = "Add new category"
//                            )
//                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    // Apply changes to all categories when "Done" is pressed
                    editableCategories.forEach { (category, nameState) ->
                        // Only rename if the category name is different and not blank
                        if (nameState.value.isNotBlank() && nameState.value != category.name) {
                            onRenameCategory(category, nameState.value.trim())
                        }
                    }

                    // Add new category if newCategoryName is not blank
                    if (newCategoryName.isNotBlank()) {
                        onAddCategory(newCategoryName.trim())
                        newCategoryName = "" // Reset after adding
                    }

                    // Delete categories marked for deletion
                    categoriesToDelete.forEach { category ->
                        onDeleteCategory(category)
                    }

                    onDismiss() // Dismiss dialog after applying changes
                }
            ) {
                Text("Done")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
