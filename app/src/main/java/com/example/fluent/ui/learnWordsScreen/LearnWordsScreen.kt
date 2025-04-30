package com.example.fluent.ui.learnWordsScreen


import androidx.annotation.Nullable
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
import androidx.compose.runtime.LaunchedEffect
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
import com.example.fluent.navigation.Screen
import com.example.fluent.ui.categorySelection.CategoriesViewModel
import com.example.fluent.ui.components.ConfirmButton
import com.example.fluent.ui.components.FullScreenBlurredBackground
import com.example.fluent.ui.components.AnimatedProgressBar
import com.example.fluent.ui.components.GlassyAlertDialog
import org.jetbrains.annotations.NotNull


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
    val totalWords = learnedWords + leftWords

    val categories = viewModel.categories.collectAsState(initial = emptyList()).value
    val selectedCategoryId = viewModel.selectedCategoryId.collectAsState().value
    val selectedCategoryName = categories.find { it.id == selectedCategoryId }?.name ?: "All"

    val showDialogFromVM by viewModel.showCompletionDialog.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(showDialogFromVM) {
        if (showDialogFromVM) {
            showDialog = true
            viewModel.onDialogShown()
        }
    }


    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        FullScreenBlurredBackground(
            navController = navController,
            showCategorySelection = true,
            categoriesViewModel = categoriesViewModel,
            categories = categories,
            selectedCategoryId = selectedCategoryId,
            onCategorySelected = { categoryId ->
                viewModel.selectCategory(categoryId)
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 80.dp)
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    // Progress Bar
                    Column(
                        modifier = Modifier.padding(start = 30.dp, end = 30.dp)
                    ) {
                        Box(modifier = Modifier.fillMaxWidth()) {
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
                                .padding(start = 12.dp)
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

                    if (showDialog) {
                        GlassyAlertDialog(
                            title = "Congrats!",
                            text = "You have finished all words from '$selectedCategoryName' category",
                            confirmButtonText = "Finish",
                            onConfirm = {
                                showDialog = false
                                viewModel.resetLearningProgress()
                                navController.navigate(Screen.Screen4.route) {
                                    popUpTo(Screen.Screen4.route) { inclusive = true }
                                }
                            },
                            onDismissRequest = { showDialog = false }
                        )
                    }


                    // Main Content
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(start = 10.dp, end = 10.dp, top = 170.dp)
                            .imePadding(),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = correctWord ?: "",
                            color = Color.Yellow,
                            modifier = Modifier
                                .offset(y = (-60).dp)
                                .height(24.dp)
                        )

                        Spacer(modifier = Modifier.height(2.dp))

                        Text(
                            text = currentWord?.translation ?: "",
                            modifier = Modifier.offset(y = (-60).dp)
                        )

                        val hasWords = totalWords > 0
                        if (hasWords) {
                            AppTextField(
                                value = userInput,
                                onValueChange = {
                                    viewModel.onEvent(
                                        WordEventForLearnWordsScreen.SetWordInputLearnWords(it)
                                    )
                                },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Text,
                                    imeAction = ImeAction.Done
                                ),
                                keyboardActions = KeyboardActions(
                                    onDone = {
                                        viewModel.onEvent(WordEventForLearnWordsScreen.CheckAnswer)
                                        // Don't hide the keyboard unless learning is done
                                        if (leftWords == 0) {
                                            keyboardController?.hide()
                                        }
                                    }
                                ),
                                label = { Text(text = "Word") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp)
                                    .offset(y = (-60).dp)
                            )

                            ConfirmButton(
                                onClick = {
                                    viewModel.onEvent(WordEventForLearnWordsScreen.CheckAnswer)
                                    if (leftWords == 0) {
                                        keyboardController?.hide()
                                    }
                                },
                                buttonText = if (correctWord != null) "Next Word" else "Confirm"
                            )
                        } else {
                            Text(text = "No words in this category")
                        }
                    }
                }
            }
        }
    }
}
