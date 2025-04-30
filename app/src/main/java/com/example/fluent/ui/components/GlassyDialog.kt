package com.example.fluent.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties


@Composable
fun GlassyDialog(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    properties: DialogProperties = DialogProperties(usePlatformDefaultWidth = false),
    title: @Composable (() -> Unit)? = null,
    text: @Composable (() -> Unit)? = null,
    confirmButton: @Composable (() -> Unit)? = null,
    dismissButton: @Composable (() -> Unit)? = null,
    content: @Composable (BoxScope.() -> Unit)? = null
) {
    val dialogShape = RoundedCornerShape(28.dp)

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = properties
    ) {
        Surface(
            modifier = modifier
                .then(
                    if (content == null) {
                        // Default size for alert-style dialog
                        Modifier.fillMaxWidth(0.95f)
                    } else {
                        // Custom content may need custom sizing
                        Modifier
                    }
                )
                .wrapContentHeight(),
            shape = dialogShape,
            color = Color.Transparent
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .clip(dialogShape)
            ) {
                // Blurred background
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .blur(100.dp)
                        .background(Color.Black.copy(alpha = 0.9f))
                        .clip(dialogShape)
                )

                // Gradient and border layer
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.White.copy(alpha = 0.01f),
                                    Color.White.copy(alpha = 0.02f)
                                )
                            )
                        )
                        .border(
                            width = Dp.Hairline,
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color.White.copy(alpha = 0.15f),
                                    Color.White.copy(alpha = 0.1f),
                                )
                            ),
                            shape = dialogShape
                        )
                )

                // Content layer
                if (content != null) {
                    // Custom content mode
                    content()
                } else {
                    // Alert dialog mode
                    Column(
                        modifier = Modifier.padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        title?.let {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 8.dp)
                            ) {
                                it()
                            }
                        }

                        text?.let {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 16.dp)
                            ) {
                                it()
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            dismissButton?.let {
                                Box(modifier = Modifier.padding(end = 8.dp)) {
                                    it()
                                }
                            }
                            confirmButton?.let { it() }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun GlassyAlertDialog(
    onDismissRequest: () -> Unit,
    title: String,
    text: String,
    confirmButtonText: String,
    onConfirm: () -> Unit,
    dismissButtonText: String? = null,
    onDismiss: (() -> Unit)? = null
) {
    GlassyDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall
            )
        },
        text = {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(confirmButtonText)
            }
        },
        dismissButton = dismissButtonText?.let {
            {
                TextButton(onClick = onDismiss ?: onDismissRequest) {
                    Text(dismissButtonText)
                }
            }
        }
    )
}