package com.example.fluent.ui.learnWordsScreen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.fluent.WordEventForLearnWordsScreen
import com.example.fluent.navigation.Screen
import com.example.fluent.ui.categorySelection.CategoriesViewModel
import com.example.fluent.ui.components.*
import androidx.compose.foundation.shape.RoundedCornerShape

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

                    //  Custom Dialog, when words finished
                    if (showDialog) {
                        Dialog(
                            onDismissRequest = { showDialog = false },
                            properties = DialogProperties(dismissOnClickOutside = false)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = Color.White,
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier
                                        .padding(24.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "🎉Congratulations!🎉",
                                        style = MaterialTheme.typography.headlineSmall,
                                        color = MaterialTheme.colorScheme.primary
                                    )

                                    Spacer(modifier = Modifier.height(16.dp))

                                    Box(
                                        modifier = Modifier
                                            .height(200.dp)
                                            .fillMaxWidth()
                                            .padding(8.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        // 🎬 Placeholder for video
                                        Text(text = "🎬 (video placeholder)", color = Color.Gray)
                                    }

                                    Spacer(modifier = Modifier.height(24.dp))

                                    Button(
                                        onClick = {
                                            showDialog = false
                                            viewModel.resetLearningProgress()
                                        }
                                    ) {
                                        Text("Start New Game")
                                    }
                                }
                            }
                        }
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
