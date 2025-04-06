package com.example.fluent.ui.categorySelection

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.fluent.data.Category

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
    val editableCategories = remember(categories) {
        categories.map { it to mutableStateOf(it.name) }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Manage Categories") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
                    .padding(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Existing Categories",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                // List of existing categories with rename and delete functionality
                if (categories.isEmpty()) {
                    Text(
                        text = "No categories yet",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                } else {
                    editableCategories.forEach { (category, nameState) ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = nameState.value,
                                onValueChange = { nameState.value = it },
                                label = { Text("Category name") },
                                singleLine = true,
                                modifier = Modifier.weight(1f)
                            )

                            IconButton(
                                onClick = { onDeleteCategory(category) },
                                modifier = Modifier.padding(start = 8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete ${category.name}",
                                    tint = Color.Red
                                )
                            }

                            IconButton(
                                onClick = {
                                    if (nameState.value.isNotBlank() && nameState.value != category.name) {
                                        onRenameCategory(category, nameState.value.trim())
                                    }
                                },
                                enabled = nameState.value.isNotBlank() && nameState.value != category.name,
                                modifier = Modifier.padding(start = 4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Save changes to ${category.name}"
                                )
                            }
                        }
                    }
                }

                Divider(modifier = Modifier.padding(vertical = 8.dp))

                // Add new category section
                Text(
                    text = "Add New Category",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = newCategoryName,
                        onValueChange = { newCategoryName = it },
                        label = { Text("New category name") },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )

                    IconButton(
                        onClick = {
                            if (newCategoryName.isNotBlank()) {
                                onAddCategory(newCategoryName.trim())
                                newCategoryName = ""
                            }
                        },
                        enabled = newCategoryName.isNotBlank(),
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add new category"
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    // Save any pending changes before dismissing
                    editableCategories.forEach { (category, nameState) ->
                        if (nameState.value.isNotBlank() && nameState.value != category.name) {
                            onRenameCategory(category, nameState.value.trim())
                        }
                    }
                    onDismiss()
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