package com.example.fluent.ui.screen2

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.fluent.WordEventForScreen2
import com.example.fluent.data.Word

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Screen2(
    itemId: Int,
    viewModel: Screen2ViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    val item = viewModel.item.collectAsState().value

    Text(text = "Item ID: $itemId")
    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = { Text("Go Back") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            onBackClick()
                            item?.let {
                                viewModel.onEvent(WordEventForScreen2.deleteWord(it))
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(
                text = item?.word ?: "",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(16.dp))
            Card {
                Text(
                    text = item?.translation ?: "",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}