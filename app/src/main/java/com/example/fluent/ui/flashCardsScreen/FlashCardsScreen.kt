package com.example.fluent.ui.flashCardsScreen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.fluent.navigation.BlurredAppNavigationBar
import com.example.fluent.ui.categorySelection.CategoriesViewModel
import com.example.fluent.ui.categorySelection.CategoryManagementDialog
import com.example.fluent.ui.categorySelection.CategorySelection
import com.example.fluent.ui.components.FlashCardsContent
import com.example.fluent.ui.components.FullScreenBlurredBackground

@Composable
fun FlashCardsScreen(
    navController: NavHostController,
    viewModel: FlashCardsViewModel = hiltViewModel(),
    categoriesViewModel: CategoriesViewModel = hiltViewModel(),
) {
    val categories = viewModel.categories.collectAsState(initial = emptyList()).value
    val selectedCategoryId = viewModel.selectedCategoryId.collectAsState().value
    val uiState by categoriesViewModel.uiState.collectAsState()
    var showCategoryManagementDialog by remember { mutableStateOf(false) }
    val cardsData by viewModel.cardsData.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 8.dp)
        ) {
            FullScreenBlurredBackground(
                blurRadius = 5.dp,
            ) {
                // Display category management dialog
                if (showCategoryManagementDialog) {
                    CategoryManagementDialog(
                        categories = uiState.categories,
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

                CategorySelection(
                    categories = categories.map { it.name },
                    selectedCategory = categories.find { it.id == selectedCategoryId }?.name.orEmpty(),
                    onCategorySelected = { selectedName ->
                        val selected = categories.find { it.name == selectedName }
                        viewModel.selectCategory(selected?.id)
                    },
                    onAddCategory = {
                        showCategoryManagementDialog = true
                    }
                )
            }
        }

        // Use the FlashCardsContent from components package
        FlashCardsContent(
            cardsData = cardsData,
            onMoveToBack = { index -> viewModel.moveCardToBack(index) }
        )

        // App navigation bar
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        ) {
            BlurredAppNavigationBar(navController = navController)
        }
    }
}