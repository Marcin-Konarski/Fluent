package com.example.fluent.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import com.example.fluent.ui.theme.DeepMagenta
import com.example.fluent.ui.theme.LocalAppTheme

@Composable
fun ConfirmButton(
    onClick: () -> Unit,
    buttonText: String // Pass custom text from the screen
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed = interactionSource.collectIsPressedAsState().value
    val scale = animateFloatAsState(if (isPressed) 0.96f else 1f, label = "")
    val appTheme = LocalAppTheme.current

    Button(
        onClick = onClick,
        modifier = Modifier
            .padding(top = 5.dp)
            .height(45.dp)
            .width(320.dp)
            .scale(scale.value)
            .offset(y = (-60).dp),
        interactionSource = interactionSource,
        colors = ButtonDefaults.buttonColors(
            containerColor = appTheme.primaryColor,
            contentColor = Color.White
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 8.dp,
            pressedElevation = 12.dp,
            focusedElevation = 12.dp
        )
    ) {
        Text(text = buttonText) // Now dynamic
    }
}
