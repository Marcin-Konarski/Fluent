package com.example.fluent.ui.screen1

import androidx.activity.OnBackPressedCallback
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Screen1(
    viewModel: Screen1ViewModel = hiltViewModel(),
    onItemClick: (Int) -> Unit,
    onButtonClick: () -> Unit
) {
    val items = viewModel.items.collectAsState(initial = emptyList()).value
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Words List") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { onButtonClick() }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add"
                )
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            items(items) { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .clickable { onItemClick(item.id) }
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = item.word,
                            style = MaterialTheme.typography.headlineSmall
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = item.translation)
                    }
                }
            }
        }
    }
}