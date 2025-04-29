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
import androidx.compose.ui.Alignment
import com.example.fluent.ui.theme.LocalAppTheme



private data class GlitterParticle(
    val id: Int,
    val initialOffset: Float,
    val y: Float,
    val color: Color,
    val speed: Float,
    val size: Float,
    val isBright: Boolean = false
)

@Composable
fun AnimatedProgressBar(progress: Float) {
    val appTheme = LocalAppTheme.current
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

    val startColor = appTheme.primaryColor
    val middleColor = appTheme.secondaryColor
    val endColor = appTheme.tertiaryColor

    val animatedBrush = Brush.horizontalGradient(
        colors = listOf(startColor, middleColor, endColor),
        startX = (gradientStartOffset - gradientOffset * 0.4f) * 1000f,
        endX = gradientEndOffset * 1000f
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(12.dp) // Zwiększona wysokość dla efektu 3D
            .drawBehind {
                val cornerRadius = 60.dp.toPx()
                // Tło dla całego paska (cień/glow)
                val paint = Paint().apply {
                    asFrameworkPaint().apply {
                        isAntiAlias = true
                        color = startColor.copy(alpha = 0.9f).toArgb()
                        maskFilter = BlurMaskFilter(40f, BlurMaskFilter.Blur.NORMAL)
                    }
                }

                drawIntoCanvas { canvas ->
                    val shift = 0.dp.toPx()
                    val glowTop = size.height / 2f - size.height * 0.6f
                    val glowBottom = size.height / 2f + size.height * 0.6f
                    canvas.drawRoundRect(
                        left = 0f,
                        top = glowTop + shift,
                        right = size.width,
                        bottom = glowBottom + shift,
                        radiusX = cornerRadius,
                        radiusY = cornerRadius,
                        paint = paint
                    )
                }
            }
    ) {
        // Główny kontener dla paska postępu
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp)
                .align(Alignment.BottomCenter) // Ustawienie na dole dla efektu 3D
                .clip(RoundedCornerShape(60.dp))
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(Color.LightGray, Color.Gray)
                    )
                )
        ) {
            // 1. Efekt glow pod spodem (dodatkowy blur dla efektu 3D)
            Box(
                modifier = Modifier
                    .fillMaxWidth(animatedProgress)
                    .height(24.dp)
                    .drawBehind {
                        val paint = Paint().apply {
                            asFrameworkPaint().apply {
                                isAntiAlias = true
                                color = appTheme.quaternaryColor.copy(alpha = 0.7f).toArgb()
                                maskFilter = BlurMaskFilter(30f, BlurMaskFilter.Blur.NORMAL)
                            }
                        }

                        drawIntoCanvas { canvas ->
                            canvas.drawRoundRect(
                                left = 0f,
                                top = -size.height / 2f,
                                right = size.width,
                                bottom = size.height * 1.8f, // Większy obszar blur na dole
                                radiusX = 60.dp.toPx(),
                                radiusY = 60.dp.toPx(),
                                paint = paint
                            )
                        }
                    }
            )

            // 2. Gradientowy pasek (animowany)
            Box(
                modifier = Modifier
                    .fillMaxWidth(animatedProgress)
                    .height(12.dp)
                    .clip(RoundedCornerShape(60.dp))
                    .background(animatedBrush)
            )

            // 3. Brokat (nad gradientem)
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
}


@Composable
private fun GlitterEffect(progress: Float, modifier: Modifier = Modifier) {
    val appTheme = LocalAppTheme.current
    val glitterParticles = remember {
        List(35) { id ->
            val isBright = id % 8 == 0
            GlitterParticle(
                id = id,
                initialOffset = Random.nextFloat(),
                y = Random.nextFloat(),
                color = when {
                    isBright -> Color.White.copy(alpha = 0.8f)
                    else -> listOf(
                        appTheme.tertiaryColor.copy(alpha = 0.8f),
                        appTheme.secondaryColor.copy(alpha = 0.8f)
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

