package com.example.fluent.ui.screen1

import androidx.lifecycle.ViewModel
import com.example.fluent.data.DataRepository
import com.example.fluent.data.SampleData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class Screen1ViewModel @Inject constructor(
    private val repository: DataRepository
) : ViewModel() {
    val items: List<SampleData> = repository.getSampleData()
}