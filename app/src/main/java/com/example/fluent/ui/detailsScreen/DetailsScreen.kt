package com.example.fluent.ui.detailsScreen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.fluent.WordEventForScreen2
import com.example.fluent.ui.components.AppButton
import com.example.fluent.ui.components.AppNavigationBar
import com.example.fluent.ui.components.AppTopBar
import com.example.fluent.ui.components.AppDeleteButton
import com.example.fluent.ui.components.AppCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Screen2(
    navController: NavHostController,
    itemId: Int,
    viewModel: DetailsViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    val item = viewModel.item.collectAsState().value

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Go Back",
                showBackButton = true,
                onBackClick = onBackClick,
                actions = {
                    AppDeleteButton(
                        onDeleteClick = {
                            onBackClick()
                            item?.let {
                                viewModel.onEvent(WordEventForScreen2.DeleteWord(it))
                            }
                        }
                    )
                }
            )
        },
        bottomBar = {
            AppNavigationBar(navController = navController)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Display the currently selected word using WordCard
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
}
