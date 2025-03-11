package com.example.fluent.ui.screen3

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.fluent.WordEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Screen3(
    viewModel: Screen3ViewModel = hiltViewModel(),
    onButtonClick: () -> Unit,
) {
    val state by viewModel.state.collectAsState()
    var word by remember { mutableStateOf("") }
    var translation by remember { mutableStateOf("") }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add new word") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                onButtonClick()
                viewModel.onEvent(WordEvent.saveWord)
            }) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Save"
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            TextField(
                value = state.word,
                onValueChange = { viewModel.onEvent(WordEvent.setWord(it)) },
                placeholder = {
                    Text(text = "word")
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = state.translation,
                onValueChange = { viewModel.onEvent(WordEvent.setTranslation(it)) },
                placeholder = {
                    Text(text = "description")
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = false,
                maxLines = 5
            )
        }
    }
}