package com.example.fluent.ui.detailsScreen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.fluent.WordEventForScreen2
import com.example.fluent.ui.components.AppButton
import com.example.fluent.ui.components.AppCard
import com.example.fluent.navigation.BlurredAppNavigationBar
import com.example.fluent.ui.components.FullScreenBlurredBackground

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    navController: NavHostController,
    itemId: Int,
    viewModel: DetailsViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    val item = viewModel.item.collectAsState().value

    Box(modifier = Modifier.fillMaxSize()) {
        FullScreenBlurredBackground(
            blurRadius = 5.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AppCard(
                    word = item?.word ?: "",
                    translation = item?.translation ?: "",
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                AppButton(
                    text = "Save",
                    onClick = { /* Save functionality */ },
                    modifier = Modifier
                )

                Text(text = "Item ID: $itemId")
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(onClick = {
                item?.let {
                    viewModel.onEvent(WordEventForScreen2.DeleteWord(it))
                    onBackClick()
                }
            }) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
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
