package com.example.fluent.ui.learnWordsScreen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fluent.WordEventForLearnWordsScreen
import com.example.fluent.data.CategoryDao
import com.example.fluent.data.Word
import com.example.fluent.data.WordDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
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

    private var testCompleted: Boolean
        get() = savedStateHandle.get<Boolean>(getTestCompletedKey()) ?: false
        set(value) = savedStateHandle.set(getTestCompletedKey(), value)

    private var savedWordOrder: List<Int>
        get() = savedStateHandle.get<List<Int>>(getWordOrderKey()) ?: emptyList()
        set(value) = savedStateHandle.set(getWordOrderKey(), value)

    private val _showCompletionDialog = MutableStateFlow(false)
    val showCompletionDialog: StateFlow<Boolean> = _showCompletionDialog

    fun onDialogShown() {
        _showCompletionDialog.value = false
    }

    init {
        println("LearnWordsViewModel initialized")
        _progress.value = 0f

        viewModelScope.launch {
            _selectedCategoryId.collectLatest { categoryId ->
                savedStateHandle["selectedCategoryId"] = categoryId
                fetchWordsForCategory(categoryId)
                _userInput.value = ""
                _correctWord.value = null
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
                    val wordIds = savedWordOrder

                    if (wordIds.isEmpty() || testCompleted) {
                        wordsList = newWords.shuffled().toMutableList()
                        savedWordOrder = wordsList.map { it.id }
                        testCompleted = false
                    } else {
                        val existingWords = mutableListOf<Word>()
                        val newWordsMap = newWords.associateBy { it.id }
                        for (id in wordIds) {
                            newWordsMap[id]?.let { existingWords.add(it) }
                        }
                        val remainingWords = newWords.filter { !wordIds.contains(it.id) }
                        existingWords.addAll(remainingWords)
                        wordsList = existingWords
                        savedWordOrder = wordsList.map { it.id }
                    }

                    if (currentIndex >= wordsList.size) {
                        currentIndex = 0
                    }

                    _currentWord.value = wordsList.getOrNull(currentIndex)
                    updateLearnedAndLeftWords()
                    calculateProgress()
                } else {
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
                    correctAnswersPerCategory = correctAnswers
                    updateLearnedAndLeftWords()

                    if (correctAnswers >= wordsList.size) {
                        testCompleted = true
                        _showCompletionDialog.value = true // ✅ Correctly trigger dialog
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
        testCompleted = true
        correctAnswers = 0
        correctAnswersPerCategory = 0
        currentIndex = 0
        _userInput.value = ""
        _correctWord.value = null
        fetchWordsForCategory(_selectedCategoryId.value)
    }
}
