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
import com.example.fluent.ui.theme.LightMagenta

@Composable
fun ProgressBar(progress: Float) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(12.dp)
            .clip(RoundedCornerShape(60.dp))
            .background(
                Brush.horizontalGradient(
                    colors = listOf(Color.LightGray, Color.Gray)
                )
            ) // Szare tło paska
            //.shadow(elevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(progress)
                .height(12.dp)
                .clip(RoundedCornerShape(60.dp))
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(OrchidMagenta, DeepMagenta)
                    )
                )
        )
    }
}
