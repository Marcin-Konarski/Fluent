package com.example.fluent.ui.flashCardsScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.fluent.R
import com.example.fluent.ui.components.BlurredAppNavigationBar
import com.example.fluent.ui.components.FullScreenBlurredBackground
import dev.chrisbanes.haze.HazeState


@Composable
fun FlashCardsScreen(
    navController: NavHostController
) {
    val hazeState = remember {
        HazeState()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Full-screen blurred background with content
        FullScreenBlurredBackground(
            wallpaperResource = R.drawable.evening_city,
            blurRadius = 0,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Your Content Here",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = { /* Your action */ }) {
                    Text(text = "Click Me")
                }
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        ) {
            BlurredAppNavigationBar(navController = navController, hazeState = hazeState)
        }
    }
}




//@Composable
//fun YourScreen() {
//    val scrollState = rememberScrollState()
//
//    Box(
//        modifier = Modifier.fillMaxSize() // Ensure it fills the whole screen
//    ) {
//        FullSizeBlur(
//            scrollState = scrollState,
//            wallpaperResource = R.drawable.evening_city,
//            blurRadius = 80,
//            color = Color.White.copy(alpha = 0.2f),
//            scale = 0.8f,
//            strokeWidth = 1.5f,
//            content = {
//                Box(
//                    modifier = Modifier.fillMaxSize() // This ensures the blurred content expands
//                ) {
//                    Column(
//                        modifier = Modifier
//                            .fillMaxSize()
//                            .padding(16.dp),
//                        verticalArrangement = Arrangement.Center,
//                        horizontalAlignment = Alignment.CenterHorizontally
//                    ) {
//                        Text(
//                            text = "Your Content Here",
//                            style = MaterialTheme.typography.headlineMedium,
//                            color = Color.White
//                        )
//
//                        Spacer(modifier = Modifier.height(16.dp))
//
//                        Button(onClick = { /* Your action */ }) {
//                            Text(text = "Click Me")
//                        }
//                    }
//                }
//            }
//        )
//    }
//}

