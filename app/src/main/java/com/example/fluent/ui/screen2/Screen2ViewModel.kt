package com.example.fluent.ui.screen2

import androidx.lifecycle.ViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.fluent.WordEventForScreen2
import com.example.fluent.WordState
import com.example.fluent.data.WordDao
import com.example.fluent.data.Word
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class Screen2ViewModel @Inject constructor(
    private val repository: WordDao,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val itemId: Int = checkNotNull(savedStateHandle["itemId"])
    val item: StateFlow<Word?> = repository.getDetailData(itemId)
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    fun onEvent(event: WordEventForScreen2){
        when(event){
            is WordEventForScreen2.DeleteWord -> {
                viewModelScope.launch {
                    repository.deleteWord(event.word)
                }
            }
        }
    }
}