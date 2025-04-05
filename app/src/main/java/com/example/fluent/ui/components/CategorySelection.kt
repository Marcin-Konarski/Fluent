package com.example.fluent.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import androidx.compose.foundation.layout.height as composableHeight

@Composable
fun CategorySelection(
    categories: List<String>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    showAllOption: Boolean = true,
    content: @Composable (String) -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
    ) {

        // Create a list that includes all option if requested
        val displayCategories = remember(categories, showAllOption) {
            if (showAllOption) {
                listOf("All") + categories
            } else {
                categories
            }
        }

        // Find the current selected index
        val initialPage = remember(selectedCategory, displayCategories) {
            if (selectedCategory.isEmpty()) 0 // "All" is at index 0
            else displayCategories.indexOf(selectedCategory).coerceAtLeast(0)
        }

        // Store the current page
        var currentPage by remember { mutableIntStateOf(initialPage) }

        // Create a simplified tab row that follows material design guidelines
        ScrollableTabRow(
            selectedTabIndex = currentPage,
            edgePadding = 16.dp,
            containerColor = Color.Transparent,
            contentColor = Color(0xFF362C28),
            divider = {},
            indicator = { tabPositions ->
                if (tabPositions.isNotEmpty() && currentPage < tabPositions.size) {
                    Box(
                        Modifier
                            .tabIndicatorOffset(tabPositions[currentPage])
                            .padding(horizontal = 16.dp)
                            .composableHeight(4.dp)
                            .background(
                                color = Color(0xFF7A6151),
                                shape = androidx.compose.foundation.shape.RoundedCornerShape(2.dp)
                            )
                    )
                }
            }
        ) {
            displayCategories.forEachIndexed { index, category ->
                Tab(
                    selected = index == currentPage,
                    onClick = {
                        currentPage = index
                        // Call the onCategorySelected callback with the appropriate value
                        val selectedValue = if (category == "All") "" else category
                        onCategorySelected(selectedValue)
                    },
                    interactionSource = remember { NoRippleInteractionSource() }
                ) {
                    Text(
                        text = category.uppercase(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    )
                }
            }
        }

        // Content area for selected category
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            // Call the provided content composable with the selected category
            val currentSelectedCategory = if (currentPage == 0 && showAllOption) ""
            else displayCategories[currentPage]
            content(currentSelectedCategory)
        }
    }
}

@Stable
class NoRippleInteractionSource : MutableInteractionSource {
    override val interactions: Flow<Interaction> = MutableSharedFlow()
    override suspend fun emit(interaction: Interaction) {}
    override fun tryEmit(interaction: Interaction) = false
}
