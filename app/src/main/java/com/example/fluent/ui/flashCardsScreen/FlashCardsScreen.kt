package com.example.fluent.ui.flashCardsScreen

import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.fluent.ui.categorySelection.CategoriesViewModel
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
    val cardsData by viewModel.cardsData.collectAsState()

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
        // Use the FlashCardsContent from components package
        FlashCardsContent(
            cardsData = cardsData,
            onMoveToBack = { index -> viewModel.moveCardToBack(index) }
        )
    }
}