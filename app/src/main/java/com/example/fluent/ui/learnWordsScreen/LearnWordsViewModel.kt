package com.example.fluent.ui.learnWordsScreen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fluent.WordEventForLearnWordsScreen
import com.example.fluent.data.CategoryDao
import com.example.fluent.data.Word
import com.example.fluent.data.WordDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LearnWordsViewModel @Inject constructor(
    private val wordDao: WordDao,
    private val categoryDao: CategoryDao,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var wordsList = mutableListOf<Word>()
    private val _currentWord = MutableStateFlow<Word?>(null)
    val currentWord = _currentWord.asStateFlow()

    private val _userInput = MutableStateFlow("")
    val userInput = _userInput.asStateFlow()

    private val _correctWord = MutableStateFlow<String?>(null)
    val correctWord = _correctWord.asStateFlow()

    private val _progress = MutableStateFlow(0f)
    val progress = _progress.asStateFlow()

    private var correctAnswers = 0

    private val _learnedWords = MutableStateFlow(0)
    val learnedWords = _learnedWords.asStateFlow()

    private val _leftWords = MutableStateFlow(0)
    val leftWords = _leftWords.asStateFlow()

    private val _selectedCategoryId = MutableStateFlow<Int?>(
        savedStateHandle.get<Int?>("selectedCategoryId")
    )
    val selectedCategoryId: StateFlow<Int?> = _selectedCategoryId

    val categories = categoryDao.getAllCategories()

    // Keys for saving state
    private fun getCurrentIndexKey(): String = "currentIndex_${_selectedCategoryId.value ?: 0}"
    private fun getCorrectAnswersKey(): String = "correctAnswers_${_selectedCategoryId.value ?: 0}"
    private fun getWordOrderKey(): String = "wordOrder_${_selectedCategoryId.value ?: 0}"
    private fun getTestCompletedKey(): String = "testCompleted_${_selectedCategoryId.value ?: 0}"

    // Persist values per category
    private var currentIndex: Int
        get() = savedStateHandle.get<Int>(getCurrentIndexKey()) ?: 0
        set(value) = savedStateHandle.set(getCurrentIndexKey(), value)

    private var correctAnswersPerCategory: Int
        get() = savedStateHandle.get<Int>(getCorrectAnswersKey()) ?: 0
        set(value) = savedStateHandle.set(getCorrectAnswersKey(), value)

    // Flag to track if test was completed (progress = 100%)
    private var testCompleted: Boolean
        get() = savedStateHandle.get<Boolean>(getTestCompletedKey()) ?: false
        set(value) = savedStateHandle.set(getTestCompletedKey(), value)

    // Save the word IDs in order to maintain word order
    private var savedWordOrder: List<Int>
        get() = savedStateHandle.get<List<Int>>(getWordOrderKey()) ?: emptyList()
        set(value) = savedStateHandle.set(getWordOrderKey(), value)

    init {
        println("LearnWordsViewModel initialized")
        _progress.value = 0f

        // Observe changes to selected category
        viewModelScope.launch {
            _selectedCategoryId.collectLatest { categoryId ->
                // Save the category ID in savedStateHandle
                savedStateHandle["selectedCategoryId"] = categoryId

                // Load words for the selected category
                fetchWordsForCategory(categoryId)

                // Reset UI states when changing categories
                _userInput.value = ""
                _correctWord.value = null

                // Load category-specific progress
                correctAnswers = correctAnswersPerCategory
                updateLearnedAndLeftWords()
                calculateProgress()
            }
        }
    }

    fun selectCategory(categoryId: Int?) {
        if (_selectedCategoryId.value != categoryId) {
            _selectedCategoryId.value = categoryId
        }
    }

    private fun fetchWordsForCategory(categoryId: Int?) {
        viewModelScope.launch {
            val wordFlow = if (categoryId == null) {
                wordDao.getAllWords()
            } else {
                wordDao.getWordsByCategoryId(categoryId)
            }

            wordFlow.collect { newWords ->
                if (newWords.isNotEmpty()) {
                    // Check if we have saved word order for this category
                    val wordIds = savedWordOrder

                    if (wordIds.isEmpty() || testCompleted) {
                        // First time loading this category OR test was completed
                        // Shuffle the words and save the order
                        wordsList = newWords.shuffled().toMutableList()
                        savedWordOrder = wordsList.map { it.id }
                        testCompleted = false
                    } else {
                        // We have a saved order - reconstruct it and append new words
                        val existingWords = mutableListOf<Word>()
                        val newWordsMap = newWords.associateBy { it.id }

                        // First add words in the saved order (if they still exist)
                        for (id in wordIds) {
                            newWordsMap[id]?.let { existingWords.add(it) }
                        }

                        // Now find words that exist in newWords but not in our saved order
                        val remainingWords = newWords.filter { !wordIds.contains(it.id) }

                        // Append new words at the end
                        existingWords.addAll(remainingWords)
                        wordsList = existingWords

                        // Update saved order to include new words
                        savedWordOrder = wordsList.map { it.id }
                    }

                    // Ensure currentIndex is valid for this word list
                    if (currentIndex >= wordsList.size) {
                        currentIndex = 0
                    }

                    _currentWord.value = wordsList.getOrNull(currentIndex)
                    updateLearnedAndLeftWords()
                    calculateProgress()
                } else {
                    // Handle empty category case
                    wordsList.clear()
                    _currentWord.value = null
                    _leftWords.value = 0
                    _learnedWords.value = 0
                    _progress.value = 0f
                }
            }
        }
    }

    fun onEvent(event: WordEventForLearnWordsScreen) {
        when (event) {
            is WordEventForLearnWordsScreen.NextWordLearnWords -> {
                if (currentIndex < wordsList.size - 1) {
                    currentIndex++
                    _currentWord.value = wordsList[currentIndex]
                    _userInput.value = ""
                    _correctWord.value = null
                    calculateProgress()
                }
            }
            is WordEventForLearnWordsScreen.SetWordLearnWords -> {
                _currentWord.value = _currentWord.value?.copy(word = event.word)
            }
            is WordEventForLearnWordsScreen.SetTranslation -> {
                _currentWord.value = _currentWord.value?.copy(translation = event.translation)
            }
            is WordEventForLearnWordsScreen.SetWordInputLearnWords -> {
                _userInput.value = event.wordInput
            }
            is WordEventForLearnWordsScreen.CheckAnswer -> {
                if (_userInput.value.equals(_currentWord.value?.word, ignoreCase = true)) {
                    _correctWord.value = null
                    correctAnswers++
                    correctAnswersPerCategory = correctAnswers // Save progress for this category
                    updateLearnedAndLeftWords()

                    // Check if all words have been learned
                    if (correctAnswers >= wordsList.size) {
                        testCompleted = true
                    }

                    onEvent(WordEventForLearnWordsScreen.NextWordLearnWords)
                } else {
                    _correctWord.value = _currentWord.value?.word
                }
                calculateProgress()
            }
        }
    }

    private fun calculateProgress() {
        val learnedWords = correctAnswers.toFloat()
        val allWordsConst = wordsList.size.toFloat()
        _progress.value = if (allWordsConst > 0) {
            learnedWords / allWordsConst
        } else {
            0f
        }
    }

    private fun updateLearnedAndLeftWords() {
        _learnedWords.value = correctAnswers
        _leftWords.value = wordsList.size - correctAnswers
    }

    fun resetLearningProgress() {
        // Mark test as completed to ensure words will be reshuffled on next load
        testCompleted = true

        // Reset progress
        correctAnswers = 0
        correctAnswersPerCategory = 0
        currentIndex = 0
        _userInput.value = ""
        _correctWord.value = null

        // Reset the word list with shuffled order
        fetchWordsForCategory(_selectedCategoryId.value)
    }

    // Reset all categories (optional functionality)
    fun resetAllCategoriesProgress() {
        viewModelScope.launch {
            // Clear all saved state for all categories
            categories.collect { categoryList ->
                categoryList.forEach { category ->
                    savedStateHandle.remove<Int>("currentIndex_${category.id}")
                    savedStateHandle.remove<Int>("correctAnswers_${category.id}")
                    savedStateHandle.remove<List<Int>>("wordOrder_${category.id}")
                    savedStateHandle.remove<Boolean>("testCompleted_${category.id}")
                }
                // Also clear for "all words" case
                savedStateHandle.remove<Int>("currentIndex_0")
                savedStateHandle.remove<Int>("correctAnswers_0")
                savedStateHandle.remove<List<Int>>("wordOrder_0")
                savedStateHandle.remove<Boolean>("testCompleted_0")
            }

            // Reset current category
            correctAnswers = 0
            correctAnswersPerCategory = 0
            currentIndex = 0
            testCompleted = true

            // Reload words
            fetchWordsForCategory(_selectedCategoryId.value)
        }
    }
}