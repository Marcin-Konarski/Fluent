package com.example.fluent.ui.flashCardsScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.fluent.WordEventForScreen2
import com.example.fluent.navigation.Screen
import com.example.fluent.ui.components.AppDeleteButton
import com.example.fluent.ui.components.AppNavigationBar
import com.example.fluent.ui.components.AppTopBar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Screen6(
    navController: NavHostController,
    viewModel: FlashCardsViewModel = hiltViewModel()
) {

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Flash Cards",
                showBackButton = false,
            )
        },
        bottomBar = {
            AppNavigationBar(navController = navController)
        }
    ){ paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ){
            Text(text = "SCREEN 6")
        }
    }
}
