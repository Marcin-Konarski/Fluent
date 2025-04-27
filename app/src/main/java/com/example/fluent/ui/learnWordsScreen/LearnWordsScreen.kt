package com.example.fluent.ui.learnWordsScreen


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.fluent.WordEventForLearnWordsScreen
import com.example.fluent.ui.components.AppTextField
import com.example.fluent.navigation.BlurredAppNavigationBar
import com.example.fluent.navigation.Screen
import com.example.fluent.ui.categorySelection.CategoriesViewModel
import com.example.fluent.ui.categorySelection.CategoryManagementDialog
import com.example.fluent.ui.categorySelection.CategorySelection
import com.example.fluent.ui.components.ConfirmButton
import com.example.fluent.ui.components.FullScreenBlurredBackground
import com.example.fluent.ui.components.AnimatedProgressBar


@Composable
fun LearnWordsScreen(
    navController: NavHostController,
    viewModel: LearnWordsViewModel = hiltViewModel(),
    categoriesViewModel: CategoriesViewModel = hiltViewModel(),
) {
    val currentWord by viewModel.currentWord.collectAsState()
    val userInput by viewModel.userInput.collectAsState()
    val correctWord by viewModel.correctWord.collectAsState()
    val progress by viewModel.progress.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val learnedWords by viewModel.learnedWords.collectAsState()
    val leftWords by viewModel.leftWords.collectAsState()

    val categories = viewModel.categories.collectAsState(initial = emptyList()).value
    val selectedCategoryId = viewModel.selectedCategoryId.collectAsState().value
    val uiState by categoriesViewModel.uiState.collectAsState()
    var showCategoryManagementDialog by remember { mutableStateOf(false) } // Variable to show/hide 'Category management' dialog

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        FullScreenBlurredBackground(
//            wallpaperResource = R.drawable.black1,
            blurRadius = 5.dp,
        ) {

            // Display category management dialog
            if (showCategoryManagementDialog) {
                CategoryManagementDialog(
                    categories = uiState.categories,
                    onDismiss = { showCategoryManagementDialog = false },
                    onAddCategory = { newCategoryName ->
                        categoriesViewModel.addCategory(newCategoryName)
                    },
                    onRenameCategory = { category, newName ->
                        categoriesViewModel.renameCategory(category, newName)
                    },
                    categoriesViewModel = categoriesViewModel
                )
            }



            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Display all categories in a row
                CategorySelection(
                    categories = categories.map { it.name },
                    selectedCategory = categories.find { it.id == selectedCategoryId }?.name.orEmpty(),
                    onCategorySelected = { selectedName ->
                        val selected = categories.find { it.name == selectedName }
                        viewModel.selectCategory(selected?.id)
                    },
                    onAddCategory = {
                        showCategoryManagementDialog = true
                    }
                )

                // Spacer(modifier = Modifier.height(5.dp))
                // Progress Bar
                Column {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 16.dp)
                    ) {
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "Your progress",
                                modifier = Modifier.padding(bottom = 4.dp),
                            )
                            AnimatedProgressBar(progress = progress)

                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                    ) {
                        Text(
                            text = "Learned: $learnedWords",
                            modifier = Modifier.offset(x = -10.dp)
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = "Left: $leftWords",
                            modifier = Modifier.offset(x = -5.dp)
                        )
                    }
                }

                // Content
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 16.dp, end = 16.dp, top = 120.dp)
                        .imePadding(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = correctWord ?: " ",
                        color = Color.Yellow,
                        modifier = Modifier
                            .offset(y = (-60).dp)
                            .height(24.dp)
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = currentWord?.translation ?: "No more words",
                        modifier = Modifier.offset(y = (-60).dp)
                    )

                    AppTextField(
                        value = userInput,
                        onValueChange = {
                            viewModel.onEvent(WordEventForLearnWordsScreen.SetWordInputLearnWords(it))
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                viewModel.onEvent(WordEventForLearnWordsScreen.CheckAnswer)
                            }
                        ),
                        label = {
                            Text(text = "Word")
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                            .offset(y = (-60).dp)
                    )

                    ConfirmButton(
                        onClick = {
                            if (leftWords == 0) {
                                keyboardController?.hide()
                                viewModel.resetLearningProgress()
                                navController.navigate(Screen.Screen4.route) {
                                    popUpTo(Screen.Screen4.route) { inclusive = true }
                                }
                            } else {
                                viewModel.onEvent(WordEventForLearnWordsScreen.CheckAnswer)
                            }
                        },
                        buttonText = if (leftWords == 0) "Finish" else if (correctWord != null) "Next Word" else "Confirm"
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
