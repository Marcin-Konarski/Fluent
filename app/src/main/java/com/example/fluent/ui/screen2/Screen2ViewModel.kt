package com.example.fluent.ui.screen2

import androidx.lifecycle.ViewModel
import androidx.lifecycle.SavedStateHandle
import com.example.fluent.data.WordDao
import com.example.fluent.data.Word
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class Screen2ViewModel @Inject constructor(
    private val repository: WordDao,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val itemId: Int = checkNotNull(savedStateHandle["itemId"])
    val item: Word = repository.getDetailData(itemId)
}