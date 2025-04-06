package com.example.fluent.ui.categorySelection

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeChild
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow



@Composable
fun CategorySelection(
    categories: List<String>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    onAddCategory: () -> Unit,
    modifier: Modifier = Modifier,
    showAllOption: Boolean = true,
    hazeState: HazeState? = null,
    accentColor: Color = Color(0xFFFA6FFF),
    content: @Composable (String) -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
    ) {
        // Create a list that includes all option if requested, and an "Add" tab at the end
        val displayCategories = remember(categories, showAllOption) {
            val baseList = if (showAllOption) listOf("All") + categories else categories
            baseList + "+ Add"
        }

        // Find the current selected index
        val initialPage = remember(selectedCategory, displayCategories) {
            if (selectedCategory.isEmpty() && showAllOption) 0
            else displayCategories.indexOf(selectedCategory).coerceAtLeast(0)
        }

        var currentPage by remember { mutableIntStateOf(initialPage) }

        // The glassmorphic tab container
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(48.dp)
                .then(
                    // Apply haze effect if hazeState is provided
                    if (hazeState != null) {
                        Modifier.hazeChild(state = hazeState, shape = RoundedCornerShape(24.dp))
                    } else {
                        Modifier
                    }
                )
                .border(
                    width = Dp.Hairline,
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.8f),
                            Color.White.copy(alpha = 0.2f),
                        ),
                    ),
                    shape = RoundedCornerShape(24.dp)
                )
        ) {
            // If no haze state is provided, use the blur effect
            if (hazeState == null) {
                // Blurred background effect
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(24.dp))
                        .background(Color.White.copy(alpha = 0.05f))
                        .blur(radius = 10.dp, edgeTreatment = BlurredEdgeTreatment.Unbounded)
                )

                // Gradient overlay
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(24.dp))
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.White.copy(alpha = 0.15f),
                                    Color.White.copy(alpha = 0.05f)
                                )
                            )
                        )
                )
            }

            // Actual tabs with indicator
            ScrollableTabRow(
                selectedTabIndex = currentPage,
                edgePadding = 8.dp,
                containerColor = Color.Transparent,
                contentColor = Color.White.copy(alpha = 0.9f),
                divider = {},
                indicator = { tabPositions ->
                    if (tabPositions.isNotEmpty() && currentPage < tabPositions.size - 1) {
                        val currentTabPosition = tabPositions[currentPage]

                        // Glowing circle indicator
                        Box(
                            Modifier.tabIndicatorOffset(currentTabPosition)
                        ) {
                            Canvas(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .blur(50.dp, edgeTreatment = BlurredEdgeTreatment.Unbounded)
                            ) {
                                drawCircle(
                                    color = accentColor.copy(alpha = 0.6f),
                                    radius = size.height / 2,
                                    center = Offset(size.width / 2, size.height / 2)
                                )
                            }

                            // Animated border effect
                            Canvas(
                                modifier = Modifier.fillMaxSize()
                            ) {
                                drawLine(
                                    brush = Brush.horizontalGradient(
                                        colors = listOf(
                                            accentColor.copy(alpha = 0f),
                                            accentColor.copy(alpha = 0.8f),
                                            accentColor.copy(alpha = 0.8f),
                                            accentColor.copy(alpha = 0f)
                                        )
                                    ),
                                    start = Offset(0f, size.height - 2f),
                                    end = Offset(size.width, size.height - 2f),
                                    strokeWidth = 2f
                                )
                            }
                        }
                    }
                }
            ) {
                displayCategories.forEachIndexed { index, category ->
                    val isSelected = index == currentPage
                    val alpha by animateFloatAsState(
                        targetValue = if (isSelected) 1f else 0.6f,
                        label = "textAlpha"
                    )
                    val scale by animateFloatAsState(
                        targetValue = if (isSelected) 1.05f else 1f,
                        label = "textScale",
                        animationSpec = spring(
                            stiffness = Spring.StiffnessLow,
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                        )
                    )

                    Tab(
                        selected = isSelected,
                        onClick = {
                            if (category == "+ Add") {
                                onAddCategory()
                                return@Tab
                            }
                            currentPage = index
                            val selectedValue = if (category == "All" && showAllOption) "" else category
                            onCategorySelected(selectedValue)
                        },
                        interactionSource = remember { NoRippleInteractionSource() }
                    ) {
                        Text(
                            text = category.uppercase(),
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = alpha),
                            modifier = Modifier
                                .scale(scale)
                                .align(Alignment.CenterHorizontally)
                                .padding(horizontal = 12.dp, vertical = 12.dp)
                        )
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            val currentSelectedCategory = if (currentPage == 0 && showAllOption) ""
            else displayCategories.getOrNull(currentPage).takeIf { it != "+ Add" } ?: ""
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