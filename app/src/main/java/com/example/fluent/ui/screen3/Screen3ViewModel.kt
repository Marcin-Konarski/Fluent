package com.example.fluent.ui.screen3

import androidx.lifecycle.ViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.fluent.WordEvent
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
class Screen3ViewModel @Inject constructor(
    private val repository: WordDao,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    // private val itemId: Int = checkNotNull(savedStateHandle["itemId"])
    // val item: SampleData = repository.getDetailData(itemId)

    val items = repository.getSampleData() // Live Flow from database
    private val _state = MutableStateFlow(WordState())
    val state = _state.asStateFlow() // Expose immutable state to UI

    fun onEvent(event: WordEvent) {
        when(event){
            is WordEvent.deleteWord -> {
                viewModelScope.launch {
                    repository.deleteWord(event.word)
                }
            }
            WordEvent.saveWord -> {
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
            is WordEvent.setWord -> {
                _state.update { it.copy(
                    word = event.word
                ) }
            }
            is WordEvent.setTranslation -> {
                _state.update { it.copy(
                    translation = event.translation
                ) }
            }
        }
    }
}