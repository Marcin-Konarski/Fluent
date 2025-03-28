package com.example.fluent.ui.listWordsScreen

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
import androidx.navigation.NavHostController
import com.example.fluent.ui.components.AppNavigationBar
import com.example.fluent.ui.components.AppTopBar
import com.example.fluent.ui.components.AppCard
import com.example.fluent.ui.components.BlurredAppCard
import com.example.fluent.ui.components.BlurredAppNavigationBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Screen1(
    navController: NavHostController,
    viewModel: ListWordsViewModel = hiltViewModel(),
    onItemClick: (Int) -> Unit,
    onButtonClick: () -> Unit
) {
    val wordList = viewModel.wordList.collectAsState(initial = emptyList()).value

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Word List",
                showBackButton = false,
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { onButtonClick() }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
            }
        },
        bottomBar = {
            BlurredAppNavigationBar(navController = navController)
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            items(wordList) { item ->
                AppCard(
                    word = item.word,
                    translation = item.translation,
                    onClick = { onItemClick(item.id) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
        }
    }
}
