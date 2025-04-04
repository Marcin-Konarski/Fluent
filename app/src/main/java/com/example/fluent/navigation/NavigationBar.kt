package com.example.fluent.navigation


import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import dev.chrisbanes.haze.HazeState
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.Dp
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.navigation.compose.currentBackStackEntryAsState


@Composable
fun BlurredAppNavigationBar(
    navController: NavHostController,
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: Screen.Screen2.route

    val selectedTabIndex = when (currentRoute) {
        Screen.Screen1.route -> 0
        Screen.Screen2.route -> 1
        Screen.Screen3.route -> 2
        Screen.Screen4.route -> 3
        Screen.Screen5.route -> 4
        else -> 1
    }

    val navBarShape = RoundedCornerShape(24.dp)

    Box(
        modifier = Modifier
            .padding(vertical = 16.dp, horizontal = 24.dp)
            .fillMaxWidth()
            .height(60.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(navBarShape)
                .background(Color.White.copy(alpha = 0.01f))
                .blur(radius = 2.dp, edgeTreatment = BlurredEdgeTreatment.Unbounded)
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(navBarShape)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.01f), // Blur inside the box
                            Color.White.copy(alpha = 0.02f)
                        )
                    )
                )
                .border(
                    width = Dp.Hairline,
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.15f), // Less intense border
                            Color.White.copy(alpha = 0.1f),
                        ),
                    ),
                    shape = navBarShape
                )
        )

        val animatedSelectedTabIndex by animateFloatAsState(
            targetValue = selectedTabIndex.toFloat(),
            label = "animatedSelectedTabIndex",
            animationSpec = spring(
                stiffness = Spring.StiffnessLow,
                dampingRatio = Spring.DampingRatioLowBouncy,
            )
        )

        val animatedColor by animateColorAsState(
            targetValue = tabs[selectedTabIndex].color,
            label = "animatedColor",
            animationSpec = spring(
                stiffness = Spring.StiffnessLow,
            )
        )

        // Glow effect
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .clip(navBarShape)
                .blur(50.dp, edgeTreatment = BlurredEdgeTreatment.Unbounded)
        ) {
            val tabWidth = size.width / tabs.size
            drawCircle(
                color = animatedColor.copy(alpha = 0.6f),
                radius = size.height / 2,
                center = Offset(
                    (tabWidth * animatedSelectedTabIndex) + tabWidth / 2,
                    size.height / 2
                )
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp)
        ) {
            BottomBarTabs(
                tabs = tabs,
                selectedTab = selectedTabIndex,
                onTabSelected = { tab ->
                    val index = tabs.indexOf(tab)
                    when (index) {
                        0 -> navController.navigate(Screen.Screen1.route)
                        1 -> navController.navigate(Screen.Screen2.route)
                        2 -> navController.navigate(Screen.Screen3.route)
                        3 -> navController.navigate(Screen.Screen4.route)
                        4 -> navController.navigate(Screen.Screen5.route)
                    }
                }
            )
        }
    }
}


