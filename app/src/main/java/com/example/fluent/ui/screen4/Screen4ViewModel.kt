package com.example.fluent.ui.screen4

import androidx.lifecycle.ViewModel
import com.example.fluent.WordEventForScreen3
import com.example.fluent.WordEventForScreen4
import com.example.fluent.WordState
import com.example.fluent.data.WordDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class Screen4ViewModel @Inject constructor(
    private val repository: WordDao
): ViewModel() {
    private val _state = MutableStateFlow(WordState())
    val state = _state.asStateFlow()

    val wordList = repository.getSampleData()

    fun onEvent(event: WordEventForScreen4){
        when(event){
            is WordEventForScreen4.SetWord -> {
                _state.update {
                    it.copy(
                        word = event.word
                    )
                }
            }

            is WordEventForScreen4.SetTranslation -> {
                _state.update {
                    it.copy(
                        translation = event.translation
                    )
                }
            }
        }
    }
}