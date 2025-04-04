package com.example.fluent.navigation

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun BottomBarTabs(
    tabs: List<BarTabs>,
    selectedTab: Int,
    onTabSelected: (BarTabs) -> Unit,
) {
    CompositionLocalProvider(
        LocalTextStyle provides LocalTextStyle.current.copy(
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
        ),
        LocalContentColor provides Color.White
    ) {
        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            for (tab in tabs) {
                val isSelected = selectedTab == tabs.indexOf(tab)
                val alpha by animateFloatAsState(
                    targetValue = if (isSelected) 1f else .35f,
                    label = "alpha"
                )
                val scale by animateFloatAsState(
                    targetValue = if (isSelected) 1f else .98f,
                    visibilityThreshold = .000001f,
                    animationSpec = spring(
                        stiffness = Spring.StiffnessLow,
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                    ),
                    label = "scale"
                )

                // Each tab's container
                Column(
                    modifier = Modifier
                        .scale(scale)
                        .alpha(alpha)
                        .fillMaxHeight()
                        .weight(1f)
                        .pointerInput(Unit) {
                            detectTapGestures {
                                onTabSelected(tab)
                            }
                        }
                        .padding(top = 6.dp), // Padding at the top to move content down
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Icon(
                        imageVector = tab.icon,
                        contentDescription = "tab ${tab.title}",
                        modifier = Modifier
                            .scale(0.85f)
//                            .padding(bottom = 2.dp) // Increased space between icon and text
                    )
                    Text(
                        text = tab.title,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis // Handle text overflow gracefully
                    )
                }
            }
        }
    }
}