package com.example.fluent.ui.addWordScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fluent.WordEventForScreen3
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

    fun onEvent(event: WordEventForScreen3) {
        when (event) {
            WordEventForScreen3.SaveWord -> {
                val word = _state.value.word.trim()
                val translation = _state.value.translation.trim()

                // Ensure both fields are filled before saving
                if (word.isNotEmpty() && translation.isNotEmpty()) {
                    viewModelScope.launch {
                        val word = Word(
                            word = word,
                            translation = translation
                        )
                        repository.insertWord(word)

                        // Clear the input fields after saving
                        _state.update { it.copy(word = "", translation = "") }
                    }
                }
            }
            is WordEventForScreen3.SetWord -> {
                _state.update {
                    it.copy(
                        word = event.word
                    )
                }
            }
            is WordEventForScreen3.SetTranslation -> {
                _state.update {
                    it.copy(
                        translation = event.translation
                    )
                }
            }
        }
    }
}