package com.example.fluent.ui.screen4

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fluent.WordEventForScreen4and5
import com.example.fluent.data.Word
import com.example.fluent.data.WordDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val repository: WordDao
): ViewModel() {
    private val _sharedState = MutableStateFlow(0)
    val sharedState = _sharedState.asStateFlow()

    private var wordsList = mutableListOf<Word>() // Store shuffled words internally
    private val _currentWord = MutableStateFlow<Word?>(null) // State for the UI
    val currentWord = _currentWord.asStateFlow() // Expose as read-only

    private val _userInput = MutableStateFlow("") // Stores the user's typed word
    val userInput = _userInput.asStateFlow() // Read-only access

    private val _correctWord = MutableStateFlow<String?>(null) // Stores correct word
    val correctWord = _correctWord.asStateFlow()

    init {
        fetchAndShuffleWords()
    }

    private fun fetchAndShuffleWords() {
        viewModelScope.launch {
            val words = repository.getAllWords() // Fetch all words (one-time)
            if (words.isNotEmpty()) {
                wordsList = words.shuffled().toMutableList() // Shuffle the list
                _currentWord.value = wordsList.firstOrNull() // Set first word
            }else {
                _currentWord.value = null
            }
        }
    }

    fun onEvent(event: WordEventForScreen4and5) {
        val word = _currentWord.value?.word?.trim() ?: "Nothing to Display"
        val translation = _currentWord.value?.translation?.trim() ?: "Nothing to Display"

        when(event) {
            is WordEventForScreen4and5.NextWord -> {
                if (wordsList.isNotEmpty()) {
                    wordsList.removeAt(0) // Remove current word
                    _currentWord.value = wordsList.firstOrNull() // Show next word or null
                    _userInput.value = ""
                    _correctWord.value = null
                }
            }
            is WordEventForScreen4and5.SetWord -> {
                _currentWord.value = _currentWord.value?.copy(word = event.word)
            }
            is WordEventForScreen4and5.SetTranslation -> {
                _currentWord.value = _currentWord.value?.copy(word = event.translation)
            }
            is WordEventForScreen4and5.SetWordInput -> {
                _userInput.value = event.wordInput
            }
            is WordEventForScreen4and5.CheckAnswer -> { // if user inputs correct word
                if (_userInput.value.equals(_currentWord.value?.word, ignoreCase = true)) {
                    _correctWord.value = null // set _correctWord to null to don't display the message
                    onEvent(WordEventForScreen4and5.NextWord) // move to the next word automatically
                } else { // set _correctWord to current word in order to display message
                    _correctWord.value = _currentWord.value?.word
                }
            }
        }
    }

    fun updateState() {
        _sharedState.value++
    }









//    val wordList = repository.getSampleData()
//
//    fun onEvent(event: WordEventForScreen4){
//        when(event){
//            is WordEventForScreen4.SetWord -> {
//                _sharedState.update {
//                    it.copy(
//                        word = event.word
//                    )
//                }
//            }
//
//            is WordEventForScreen4.SetTranslation -> {
//                _sharedState.update {
//                    it.copy(
//                        translation = event.translation
//                    )
//                }
//            }
//        }
//    }
}