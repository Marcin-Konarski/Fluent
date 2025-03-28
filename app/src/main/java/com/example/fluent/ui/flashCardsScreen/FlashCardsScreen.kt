package com.example.fluent.ui.flashCardsScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.fluent.R
import com.example.fluent.WordEventForScreen2
import com.example.fluent.navigation.Screen
import com.example.fluent.ui.components.AppDeleteButton
import com.example.fluent.ui.components.AppNavigationBar
import com.example.fluent.ui.components.AppTopBar
import com.example.fluent.ui.components.BlurredAppNavigationBar
import com.example.fluent.ui.components.FullScreenBlurredBackground


//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun Screen6(
//    navController: NavHostController,
//    viewModel: FlashCardsViewModel = hiltViewModel()
//) {
//
//    Scaffold(
//        topBar = {
//            AppTopBar(
//                title = "Flash Cards",
//                showBackButton = false,
//            )
//        },
//        bottomBar = {
//            AppNavigationBar(navController = navController)
//        }
//    ){ paddingValues ->
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(paddingValues)
//                .padding(16.dp)
//        ){
//            Text(text = "SCREEN 6")
//        }
//    }
//}





@Composable
fun Screen6(
    navController: NavHostController
) {
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
            BlurredAppNavigationBar(navController = navController)
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

