package com.example.fluent.ui.screen2

import androidx.lifecycle.ViewModel
import androidx.lifecycle.SavedStateHandle
import com.example.fluent.data.DataRepository
import com.example.fluent.data.SampleData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class Screen2ViewModel @Inject constructor(
    private val repository: DataRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val itemId: Int = checkNotNull(savedStateHandle["itemId"])
    val item: SampleData = repository.getDetailData(itemId)
}