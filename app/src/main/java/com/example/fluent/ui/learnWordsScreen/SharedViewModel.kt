package com.example.fluent.ui.learnWordsScreen

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

    // Persist only the index
    private var currentIndex: Int
        get() = savedStateHandle.get("currentIndex") ?: 0
        set(value) = savedStateHandle.set("currentIndex", value)

    init {
        println("SharedViewModel initialized")
        if (wordsList.isEmpty()) { // Load words only if not already stored
            fetchAndShuffleWords()
        } else {
            _currentWord.value = wordsList.getOrNull(currentIndex)
        }
    }

    private fun fetchAndShuffleWords() {
        viewModelScope.launch {
            val words = repository.getAllWords() // Fetch all words (one-time)
            if (words.isNotEmpty()) {
                wordsList = words.shuffled().toMutableList() // Shuffle the list once
                currentIndex = 0
                _currentWord.value = wordsList.firstOrNull() // Set first word
            }
        }
    }

    fun onEvent(event: WordEventForScreen4and5) {
        when (event) {
            is WordEventForScreen4and5.NextWord -> {
                if (currentIndex < wordsList.size - 1) {
                    currentIndex++ // Persist index
                    _currentWord.value = wordsList[currentIndex]
                    _userInput.value = ""
                    _correctWord.value = null
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
                    onEvent(WordEventForScreen4and5.NextWord) // Move to next word automatically
                } else {
                    _correctWord.value = _currentWord.value?.word
                }
            }
        }
    }

    fun updateState() {
        _sharedState.value++
    }
}
