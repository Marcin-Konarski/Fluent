package com.example.fluent.ui.components

import androidx.compose.foundation.Image
import com.example.fluent.R
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp


@Composable
fun FullScreenBlurredBackground(
    modifier: Modifier = Modifier,
    wallpaperResource: Int = R.drawable.black1,
    blurRadius: Dp = 30.dp,
    content: @Composable () -> Unit
) {
    Box(modifier = modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = wallpaperResource),
            contentDescription = "Wallpaper",
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer { alpha = 0.9f }
                .blur(blurRadius),
            contentScale = ContentScale.Crop
        )

        // Overlay UI content on top
        Box(modifier = Modifier.fillMaxSize()) {
            content()
        }
    }
}

