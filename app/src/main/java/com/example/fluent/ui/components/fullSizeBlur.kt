package com.example.fluent.ui.components

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import com.example.fluent.R
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import dev.jakhongirmadaminov.glassmorphiccomposables.GlassmorphicColumn
import dev.jakhongirmadaminov.glassmorphiccomposables.Place
import dev.jakhongirmadaminov.glassmorphiccomposables.fastblur
import dev.shreyaspatil.capturable.Capturable
import dev.shreyaspatil.capturable.controller.CaptureController
import dev.shreyaspatil.capturable.controller.rememberCaptureController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize


@Composable
fun FullSizeBlur(
    modifier: Modifier = Modifier,
    alpha: Float = 1F,
    scrollState: ScrollState = rememberScrollState(),
    captureController: CaptureController = rememberCaptureController(),
    wallpaperResource: Int = R.drawable.evening_city,
    blurRadius: Int = 100,
    color: androidx.compose.ui.graphics.Color = androidx.compose.ui.graphics.Color.Gray,
    scale: Float = 1f,
    strokeWidth: Float = 1f,
    content: @Composable () -> Unit
) {
    val capturedBitmap = remember { mutableStateOf<Bitmap?>(null) }

    Box(
        modifier = modifier.fillMaxSize().alpha(alpha)
    ) {
        Capturable(
            controller = captureController,
            onCaptured = { bitmap, _ ->
                bitmap?.let {
                    val blurred = fastblur(it.asAndroidBitmap(), scale = 1f, blurRadius)
                    capturedBitmap.value = blurred
                }
            }
        ) {
            Image(
                painter = painterResource(id = wallpaperResource),
                contentDescription = "wallpaper",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        LaunchedEffect(Unit) {
            withContext(Dispatchers.Main) {
                if (capturedBitmap.value == null) captureController.capture()
            }
        }

        val childMeasures = remember { mutableStateListOf(Place()) }

        capturedBitmap.value?.let { capturedImage ->
            GlassmorphicColumn(
                scrollState = scrollState,
                childMeasures = childMeasures,
                targetBitmap = capturedImage,
                blurRadius = blurRadius,
                drawOnTop = { path ->
                    drawPath(path = path, color = color, style = Stroke(strokeWidth))
                },
                content = {
                    Box(
                        modifier = Modifier.fillMaxSize()
                            .onGloballyPositioned {
                                if (childMeasures.isNotEmpty()) {
                                    childMeasures[0].size = IntSize(it.size.width, it.size.height)
                                    childMeasures[0].offset = Offset(it.positionInParent().x, it.positionInParent().y)
                                }
                            }
                    ) {
                        content()
                    }
                }
            )
        }
    }
}











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

