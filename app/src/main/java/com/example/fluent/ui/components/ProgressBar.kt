package com.example.fluent.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.fluent.ui.theme.DeepMagenta
import com.example.fluent.ui.theme.OrchidMagenta
import com.example.fluent.ui.theme.VividMagenta
import androidx.compose.animation.core.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.geometry.Offset


@Composable
fun AnimatedProgressBar(progress: Float) {
    // Animowana wartość postępu
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 600),
        label = "ProgressAnimation"
    )

    // Przesuwający się gradient (fioletowa ciecz)
    val infiniteTransition = rememberInfiniteTransition(label = "ShimmerEffect")
    val offsetX by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "GradientShift"
    )

    val animatedBrush = Brush.linearGradient(
        colors = listOf(DeepMagenta, VividMagenta),
        start = Offset(offsetX, 0f),
        end = Offset(offsetX + 300f, 0f)
    )

    // Kontener paska
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(12.dp)
            .clip(RoundedCornerShape(60.dp))
            .background(
                Brush.horizontalGradient(
                    colors = listOf(Color.LightGray, Color.Gray)
                )
            )
    ) {
        // Wypełnienie z gradientem cieczy
        Box(
            modifier = Modifier
                .fillMaxWidth(animatedProgress)
                .height(12.dp)
                .clip(RoundedCornerShape(60.dp))
                .background(animatedBrush)
        )
    }
}
