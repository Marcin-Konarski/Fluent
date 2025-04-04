package com.example.fluent.ui.learnWordsScreen

import androidx.compose.ui.geometry.isEmpty
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.example.fluent.WordEventForScreen4and5
import com.example.fluent.data.Word
import com.example.fluent.data.WordDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val repository: WordDao,
    private val savedStateHandle: SavedStateHandle // Persist state across screen changes
) : ViewModel() {

    private val _sharedState = MutableStateFlow(0)
    val sharedState = _sharedState.asStateFlow()

    private var wordsList = mutableListOf<Word>() // Store shuffled words internally
    private val _currentWord = MutableStateFlow<Word?>(null) // State for the UI
    val currentWord = _currentWord.asStateFlow() // Expose as read-only

    private val _userInput = MutableStateFlow("") // Stores the user's typed word
    val userInput = _userInput.asStateFlow() // Read-only access

    private val _correctWord = MutableStateFlow<String?>(null) // Stores correct word
    val correctWord = _correctWord.asStateFlow()

    private val _progress = MutableStateFlow(0f)
    val progress = _progress.asStateFlow()

    private var correctAnswers = 0

    private val _learnedWords = MutableStateFlow(0)
    val learnedWords = _learnedWords.asStateFlow()

    private val _leftWords = MutableStateFlow(0)
    val leftWords = _leftWords.asStateFlow()

    // Persist only the index
    private var currentIndex: Int
        get() = savedStateHandle.get("currentIndex") ?: 0
        set(value) = savedStateHandle.set("currentIndex", value)

    init {
        println("SharedViewModel initialized")
        _progress.value = 0f
        if (wordsList.isEmpty()) { // Load words only if not already stored
            fetchAndShuffleWords()
        } else {
            _currentWord.value = wordsList.getOrNull(currentIndex)
            updateLearnedAndLeftWords()
        }
    }

    // Adjust the fetchAndShuffleWords function to be more robust
    private fun fetchAndShuffleWords() {
        viewModelScope.launch {
            val words = repository.getAllWords() // Fresh fetch from database

            if (words.isNotEmpty()) {
                wordsList = words.shuffled().toMutableList() // Get fresh shuffled list
                currentIndex = 0
                _currentWord.value = wordsList.firstOrNull() // Set first word
                updateLearnedAndLeftWords()
                calculateProgress()
            } else {
                // Handle empty database case
                _currentWord.value = null
                _leftWords.value = 0
                _learnedWords.value = 0
                _progress.value = 0f
            }
        }
    }

    fun onEvent(event: WordEventForScreen4and5) {
        when (event) {
            is WordEventForScreen4and5.NextWord -> {
                if (currentIndex < wordsList.size - 1) {
                    currentIndex++ // Persist index
                    _currentWord.value = wordsList[currentIndex]
                    _userInput.value = "" // Clear input but don't affect keyboard
                    _correctWord.value = null
                    calculateProgress()
                }
            }
            is WordEventForScreen4and5.SetWord -> {
                _currentWord.value = _currentWord.value?.copy(word = event.word)
            }
            is WordEventForScreen4and5.SetTranslation -> {
                _currentWord.value = _currentWord.value?.copy(translation = event.translation)
            }
            is WordEventForScreen4and5.SetWordInput -> {
                _userInput.value = event.wordInput
            }
            is WordEventForScreen4and5.CheckAnswer -> {
                if (_userInput.value.equals(_currentWord.value?.word, ignoreCase = true)) {
                    _correctWord.value = null
                    correctAnswers++
                    updateLearnedAndLeftWords()
                    // Go to next word but maintain keyboard focus
                    onEvent(WordEventForScreen4and5.NextWord)
                } else {
                    // Show correct word but don't clear input
                    _correctWord.value = _currentWord.value?.word
                    // Do not clear _userInput.value here to preserve text for correction
                    // This allows the user to see their mistake and correct it
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

    // Modify the resetLearningProgress function to refresh from database
    fun resetLearningProgress() {
        correctAnswers = 0
        currentIndex = 0
        _userInput.value = ""
        _correctWord.value = null

        // Clear the existing list to force a fresh database fetch
        wordsList.clear()

        // Fetch fresh data from database instead of reshuffling existing list
        fetchAndShuffleWords()
    }
}