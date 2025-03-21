package com.example.fluent.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    title: String,
    modifier: Modifier = Modifier,
    showBackButton: Boolean = false,
    onBackClick: (() -> Unit)? = null,
    actions: @Composable (() -> Unit)? = null
) {
    if (showBackButton && onBackClick != null) {
        TopAppBar(
            title = { Text(title) },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            },
            actions = { actions?.invoke() },
            modifier = modifier
        )
    } else {
        TopAppBar(
            title = { Text(title) },
            actions = { actions?.invoke() },
            modifier = modifier
        )
    }
}