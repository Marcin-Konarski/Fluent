package com.example.fluent.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.fluent.data.Category
import com.example.fluent.navigation.BlurredAppNavigationBar
import com.example.fluent.ui.categorySelection.CategoriesViewModel
import com.example.fluent.ui.categorySelection.CategoryManagementDialog
import com.example.fluent.ui.categorySelection.CategorySelection
import com.example.fluent.ui.theme.BackGroundColor

@Composable
fun FullScreenBlurredBackground(
    modifier: Modifier = Modifier,
    navController: NavHostController? = null,
    showCategorySelection: Boolean = false,
    categoriesViewModel: CategoriesViewModel? = null,
    categories: List<Category> = emptyList(),
    selectedCategoryId: Int? = null,
    onCategorySelected: (Int?) -> Unit = {},
    showAllCategoryOption: Boolean = true,
    mainContent: @Composable () -> Unit
) {
    var showCategoryManagementDialog by remember { mutableStateOf(false) }
    val uiState by categoriesViewModel?.uiState?.collectAsState() ?: remember { mutableStateOf(null) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = BackGroundColor)
    ) {

        // Content container
        Box(modifier = Modifier.fillMaxSize()) {
            // Main content (will be provided by screen using this component)
            mainContent()

            // Display CategoryManagementDialog if shown
            if (showCategorySelection && showCategoryManagementDialog && categoriesViewModel != null && uiState != null) {
                CategoryManagementDialog(
                    categories = uiState!!.categories,
                    onDismiss = { showCategoryManagementDialog = false },
                    onAddCategory = { newCategoryName ->
                        categoriesViewModel.addCategory(newCategoryName)
                    },
                    onRenameCategory = { category, newName ->
                        categoriesViewModel.renameCategory(category, newName)
                    },
                    categoriesViewModel = categoriesViewModel
                )
            }

            // Overlay CategorySelection at the top if requested
            if (showCategorySelection) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopCenter),
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0f)
                ) {
                    CategorySelection(
                        categories = categories.map { it.name },
                        selectedCategory = categories.find { it.id == selectedCategoryId }?.name.orEmpty(),
                        onCategorySelected = { selectedName ->
                            val selected = categories.find { it.name == selectedName }
                            onCategorySelected(selected?.id)
                        },
                        onAddCategory = {
                            showCategoryManagementDialog = true
                        },
                        showAllOption = showAllCategoryOption
                    )
                }
            }

            if (navController != null) {
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
}