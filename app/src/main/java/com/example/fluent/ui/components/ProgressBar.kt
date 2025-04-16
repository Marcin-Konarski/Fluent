package com.example.fluent.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.unit.dp
import com.example.fluent.ui.theme.DeepMagenta
import com.example.fluent.ui.theme.OrchidMagenta
import com.example.fluent.ui.theme.VividMagenta
import com.example.fluent.ui.theme.NeonMagenta
import androidx.compose.foundation.Canvas
import androidx.compose.ui.geometry.Offset
import kotlin.math.PI
import kotlin.math.sin
import kotlin.random.Random
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.geometry.CornerRadius
import android.graphics.RenderEffect
import android.graphics.Shader
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.toArgb
import android.graphics.BlurMaskFilter



import androidx.compose.ui.unit.dp


private data class GlitterParticle(
    val id: Int,
    val initialOffset: Float,
    val y: Float,
    val color: Color,
    val speed: Float,
    val size: Float,
    val isBright: Boolean = false // czy to jasna cząstka z blur
)

@Composable
fun AnimatedProgressBar(progress: Float) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 600),
        label = "ProgressAnimation"
    )

    val infiniteTransition = rememberInfiniteTransition(label = "GradientOffsetAnimation")
    val gradientOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "GradientOffset"
    )

    var gradientStartOffset by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(animatedProgress) {
        animate(
            initialValue = gradientStartOffset,
            targetValue = animatedProgress,
            animationSpec = tween(durationMillis = 600),
        ) { value, _ ->
            gradientStartOffset = value
        }
    }

    val gradientEndOffset = gradientStartOffset + (0.3f + gradientOffset * 0.2f)

    val startColor = DeepMagenta
    val middleColor = OrchidMagenta
    val endColor = VividMagenta

    val animatedBrush = Brush.horizontalGradient(
        colors = listOf(startColor, middleColor, endColor),
        startX = (gradientStartOffset - gradientOffset * 0.4f) * 1000f,
        endX = gradientEndOffset * 1000f
    )

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
        Box(
            modifier = Modifier
                .fillMaxWidth(animatedProgress)
                .height(12.dp)
                .clip(RoundedCornerShape(60.dp))
                .drawBehind {
                    // Glow efekt za paskiem
                    drawRoundRect(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                NeonMagenta.copy(alpha = 0.6f),
                                Color.Transparent
                            ),
                            center = center,
                            radius = size.height * 3f
                        ),
                        size = size,
                        cornerRadius = CornerRadius(60f, 60f)
                    )
                }
                .drawBehind {
                    val paint = Paint().apply {
                        asFrameworkPaint().apply {
                            isAntiAlias = true
                            color = NeonMagenta.copy(alpha = 0.6f).toArgb()
                            maskFilter = BlurMaskFilter(40f, BlurMaskFilter.Blur.NORMAL)
                        }
                    }

                    drawIntoCanvas { canvas ->
                        canvas.drawRoundRect(
                            left = 0f,
                            top = -size.height / 2f,
                            right = size.width,
                            bottom = size.height * 1.5f,
                            radiusX = 60.dp.toPx(),
                            radiusY = 60.dp.toPx(),
                            paint = paint
                        )
                    }


    }
                .background(animatedBrush)
        )




        if (animatedProgress > 0.01f) {
            GlitterEffect(
                progress = animatedProgress,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp)
                    .clip(RoundedCornerShape(60.dp))
            )
        }
    }
}

@Composable
private fun GlitterEffect(progress: Float, modifier: Modifier = Modifier) {
    val glitterParticles = remember {
        List(35) { id ->
            val isBright = id % 8 == 0 // co któryś brokat będzie jasny i z blur
            GlitterParticle(
                id = id,
                initialOffset = Random.nextFloat(),
                y = Random.nextFloat(),
                color = when {
                    isBright -> Color.White.copy(alpha = 0.8f)
                    else -> listOf(
                        VividMagenta.copy(alpha = 0.8f),
                        OrchidMagenta.copy(alpha = 0.8f)
                    ).random()
                },
                speed = 0.3f + Random.nextFloat() * 0.4f,
                size = if (isBright) 1.8f + Random.nextFloat() * 1.2f else 0.6f + Random.nextFloat() * 0.8f,
                isBright = isBright
            )
        }
    }

    var timeNanos by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(Unit) {
        val startTime = System.nanoTime()
        while (true) {
            val currentTime = System.nanoTime()
            timeNanos = (currentTime - startTime) / 1_000_000_000f
            kotlinx.coroutines.delay(16)
        }
    }

    Canvas(modifier = modifier) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        val progressWidth = canvasWidth * progress

        drawIntoCanvas { canvas ->
            glitterParticles.forEach { particle ->
                val x = progressWidth - ((particle.initialOffset + timeNanos * particle.speed) % 1f) * progressWidth
                if (x in 0f..progressWidth) {
                    val phaseShift = particle.id * 17
                    val pulse = 0.6f + 0.4f * sin((timeNanos * 4f + phaseShift) * particle.speed)
                    val driftY = 0.01f * sin(timeNanos * 3f + particle.id)
                    val radius = particle.size.dp.toPx() * pulse
                    val center = Offset(x, (particle.y + driftY) * canvasHeight)

                    if (particle.isBright) {
                        // Glow aura effect
                        drawCircle(
                            color = particle.color.copy(alpha = 0.1f),
                            radius = radius * 3f,
                            center = center
                        )
                        drawCircle(
                            color = particle.color.copy(alpha = 0.2f),
                            radius = radius * 2f,
                            center = center
                        )
                        drawCircle(
                            color = particle.color.copy(alpha = 0.4f),
                            radius = radius * 1.4f,
                            center = center
                        )
                    }

// Główna cząstka (zawsze rysowana)
                    drawCircle(
                        color = particle.color,
                        radius = radius,
                        center = center
                    )
                    }
                }
            }
        }
    }

