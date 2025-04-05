package com.example.fluent.ui.addWordScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fluent.WordEventForAddWord
import com.example.fluent.WordState
import com.example.fluent.data.Word
import com.example.fluent.data.WordDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class AddWordViewModel @Inject constructor(
    private val repository: WordDao,
) : ViewModel() {

    private val _state = MutableStateFlow(WordState())
    val state = _state.asStateFlow()

    init {
        // Load existing categories when ViewModel initializes
        viewModelScope.launch {
            repository.getAllCategories().collect { categories ->
                _state.update { currentState ->
                    currentState.copy(allCategories = categories)
                }
            }
        }
    }

    fun onEvent(event: WordEventForAddWord) {
        when (event) {
            WordEventForAddWord.SaveWordAddWord -> {
                val word = _state.value.word.trim()
                val translation = _state.value.translation.trim()
                val category = _state.value.category.trim()

                // Ensure all fields are filled before saving
                if (word.isNotEmpty() && translation.isNotEmpty() && category.isNotEmpty()) {
                    viewModelScope.launch {
                        val wordEntity = Word(
                            word = word,
                            translation = translation,
                            category = category
                        )
                        repository.insertWord(wordEntity)

                        // Clear the input fields after saving
                        _state.update { currentState ->
                            currentState.copy(word = "", translation = "")
                        }
                    }
                }
            }
            is WordEventForAddWord.SetWordAddWord -> {
                _state.update { currentState ->
                    currentState.copy(word = event.word)
                }
            }
            is WordEventForAddWord.SetTranslation -> {
                _state.update { currentState ->
                    currentState.copy(translation = event.translation)
                }
            }
            is WordEventForAddWord.SetCategory -> {
                _state.update { currentState ->
                    currentState.copy(category = event.category)
                }
            }
        }
    }
}