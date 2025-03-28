package com.example.fluent.ui.components

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
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
import dev.jakhongirmadaminov.glassmorphiccomposables.fastblur
import dev.shreyaspatil.capturable.Capturable
import dev.shreyaspatil.capturable.controller.rememberCaptureController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun AppCard(
    word: String,
    translation: String,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    ElevatedCard(
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 4.dp,
            pressedElevation = 8.dp,
            hoveredElevation = 10.dp,
            focusedElevation = 6.dp
        ),
        shape = MaterialTheme.shapes.medium,
        modifier = modifier
            .then(
                if (onClick != null) {
                    Modifier.clickable(onClick = onClick)
                } else {
                    Modifier
                }
            )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = word,
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = translation)
        }
    }
}








@Composable
fun BlurredAppCard(
    word: String,
    translation: String,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    blurRadius: Int = 15
) {
    val captureController = rememberCaptureController()
    val capturedBitmap = remember { mutableStateOf<Bitmap?>(null) }

    Box(modifier = modifier) {
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
                    .height(120.dp)
                    .background(Color.Transparent)
            )
        }

        capturedBitmap.value?.let { blurredImage ->
            Image(
                bitmap = blurredImage.asImageBitmap(),
                contentDescription = "Blurred Card Background",
                modifier = Modifier.fillMaxWidth().height(120.dp),
                contentScale = ContentScale.Crop,
                alpha = 0.9f
            )
        }

        LaunchedEffect(Unit) {
            withContext(Dispatchers.Main) {
                if (capturedBitmap.value == null) captureController.capture()
            }
        }

        ElevatedCard(
            elevation = CardDefaults.elevatedCardElevation(
                defaultElevation = 4.dp,
                pressedElevation = 8.dp,
                hoveredElevation = 10.dp,
                focusedElevation = 6.dp
            ),
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier
                .fillMaxWidth()
                .then(
                    if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier
                )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = word,
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = translation, color = Color.White)
            }
        }
    }
}
