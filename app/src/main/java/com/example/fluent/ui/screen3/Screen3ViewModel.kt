package com.example.fluent.ui.screen3

import androidx.lifecycle.ViewModel
import androidx.lifecycle.SavedStateHandle
import com.example.fluent.data.WordDao
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class Screen3ViewModel @Inject constructor(
    private val repository: WordDao,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    // private val itemId: Int = checkNotNull(savedStateHandle["itemId"])
    // val item: SampleData = repository.getDetailData(itemId)

    fun addNewWord(){

    }
}