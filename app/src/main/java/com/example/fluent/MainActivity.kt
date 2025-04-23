package com.example.fluent

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.fluent.navigation.AppNavigation
import com.example.fluent.ui.theme.AppTheme
import com.example.fluent.ui.theme.FluentTheme
import com.example.fluent.ui.theme.LocalAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            // Zmienna do przechowywania aktualnego motywu
            var appTheme by remember { mutableStateOf(AppTheme.MAGENTA) }

            // Provider dla aktualnego motywu
            CompositionLocalProvider(LocalAppTheme provides appTheme) {
                // Główny motyw aplikacji
                FluentTheme(appTheme = appTheme) {
                    // Powierzchnia z tłem zdefiniowanym w motywie
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        // Nawigacja, przekazanie funkcji do zmiany motywu
                        AppNavigation(navController = navController, onThemeChange = { newTheme ->
                            appTheme = newTheme
                        })
                    }
                }
            }
        }
    }
}