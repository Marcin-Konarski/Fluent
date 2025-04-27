package com.example.fluent.ui.categorySelection

import androidx.compose.foundation.background
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.fluent.data.Category
import com.example.fluent.ui.components.AppTextField

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties


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

    // Use Dialog instead of AlertDialog for more customization
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        val dialogShape = RoundedCornerShape(28.dp)

        // Overall container
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .wrapContentHeight(),
            shape = dialogShape,
            color = Color.Transparent // Important! We don't want default Surface color
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.95f)
                    .wrapContentHeight()
                    .clip(dialogShape)
            ) {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .blur(50.dp)
                        .background(Color.Black.copy(alpha = 0.7f))
                        .clip(dialogShape)
                )

                // Gradient and border layer (your previous design)
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.White.copy(alpha = 0.01f),
                                    Color.White.copy(alpha = 0.02f)
                                )
                            )
                        )
                        .border(
                            width = Dp.Hairline,
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color.White.copy(alpha = 0.15f),
                                    Color.White.copy(alpha = 0.1f),
                                )
                            ),
                            shape = dialogShape
                        )
                )





                // Content layer (non-blurred)
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
            }
        }
    }

    // Delete warning dialog (separate AlertDialog)
    if (showDeleteWarningFor != null) {
        AlertDialog(
            onDismissRequest = { showDeleteWarningFor = null },
            title = { Text("Delete Category") },
            text = {
                Text("This will also delete all words inside '${showDeleteWarningFor!!.name}'. Are you sure?")
            },
            confirmButton = {
                TextButton(onClick = {
                    categoriesToDelete.add(showDeleteWarningFor!!)
                    showDeleteWarningFor = null
                }) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteWarningFor = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}