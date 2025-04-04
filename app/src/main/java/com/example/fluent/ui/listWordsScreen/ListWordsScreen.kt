package com.example.fluent.ui.listWordsScreen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.fluent.ui.components.FullScreenBlurredBackground
import com.example.fluent.navigation.BlurredAppNavigationBar
import com.example.fluent.ui.components.GlossyAppCard



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListWordsScreen(
    navController: NavHostController,
    viewModel: ListWordsViewModel = hiltViewModel(),
    onItemClick: (Int) -> Unit,
    onButtonClick: () -> Unit
) {
    val wordList = viewModel.wordList.collectAsState(initial = emptyList()).value

    Box(modifier = Modifier.fillMaxSize()) {
        FullScreenBlurredBackground(
            blurRadius = 5.dp
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                items(wordList) { item ->
                    GlossyAppCard(
                        word = item.word,
                        translation = item.translation,
                        onClick = { onItemClick(item.id) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(90.dp)
                            .padding(vertical = 8.dp)
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        ) {
            BlurredAppNavigationBar(navController = navController)
        }
    }
}