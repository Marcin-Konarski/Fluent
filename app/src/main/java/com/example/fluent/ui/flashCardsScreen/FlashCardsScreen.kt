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
import com.example.fluent.navigation.BlurredAppNavigationBar
import com.example.fluent.ui.components.FullScreenBlurredBackground
import dev.chrisbanes.haze.HazeState


@Composable
fun FlashCardsScreen(
    navController: NavHostController
) {
    val hazeState = remember {
        HazeState()
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        FullScreenBlurredBackground(
            wallpaperResource = R.drawable.black1,
            blurRadius = 30f,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Flash Cards",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = { /* WE COOKING HERE */ }) {
                    Text(text = "Next Card")
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


