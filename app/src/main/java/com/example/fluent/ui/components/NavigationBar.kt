package com.example.fluent.ui.components

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.fluent.navigation.NavigationBar as NavBarItem
import com.example.fluent.navigation.Screen
import dev.jakhongirmadaminov.glassmorphiccomposables.fastblur
import dev.shreyaspatil.capturable.Capturable
import dev.shreyaspatil.capturable.controller.rememberCaptureController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


@Composable
fun AppNavigationBar(
    navController: NavHostController,
    modifier: Modifier = Modifier
){
    val navigationList = listOf(
        NavBarItem("Screen1", Icons.Default.Apps),
        NavBarItem("Screen4", Icons.Default.Home),
        NavBarItem("Screen6", Icons.Default.Settings)
    )

    val currentDestination = navController.currentBackStackEntry?.destination?.route
    val selectedIndex = when (currentDestination) {
        Screen.Screen1.route -> 0
        Screen.Screen4.route -> 1
        Screen.Screen5.route -> 1
        Screen.Screen6.route -> 2
        else -> 0
    }

    NavigationBar(
        containerColor = Color.Transparent,
        modifier = modifier
    ) {
        navigationList.forEachIndexed { index, navigation ->
            NavigationBarItem(
                selected = selectedIndex == index,
                onClick = {
                    when (index) {
                        0 -> navController.navigate(Screen.Screen1.route)
                        1 -> navController.navigate(Screen.Screen4.route)
                        2 -> navController.navigate(Screen.Screen6.route)
                    }
                },
                icon = {
                    Icon(imageVector = navigation.icon, contentDescription = "Icon")
                },
                label = {
                    Text(text = navigation.label)
                }
            )
        }
    }
}




@Composable
fun BlurredAppNavigationBar(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    blurRadius: Int = 20
) {
    val captureController = rememberCaptureController()
    val capturedBitmap = remember { mutableStateOf<Bitmap?>(null) }

    Box(modifier = modifier.fillMaxWidth()) {
        // Captures screen content before drawing navigation bar
        Capturable(
            controller = captureController,
            onCaptured = { bitmap, _ ->
                bitmap?.let {
                    capturedBitmap.value = fastblur(it.asAndroidBitmap(), scale = 1f, blurRadius)
                }
            }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)  // Same height as Navigation Bar
            )
        }

        // Apply blurred background overlay
        capturedBitmap.value?.let { blurredImage ->
            Image(
                bitmap = blurredImage.asImageBitmap(),
                contentDescription = "Blurred Background",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(Color(0x66FFFFFF)), // Semi-transparent white for better blur visibility
                contentScale = ContentScale.Crop,
                alpha = 0.8f  // Adjust blur visibility
            )
        }

        // Capture screen after initial composition
        LaunchedEffect(Unit) {
            withContext(Dispatchers.Main) {
                captureController.capture()
            }
        }

        // Render Navigation Bar over the blurred background
        AppNavigationBar(
            navController = navController,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Transparent) // Ensure navigation bar itself remains transparent
        )
    }
}

