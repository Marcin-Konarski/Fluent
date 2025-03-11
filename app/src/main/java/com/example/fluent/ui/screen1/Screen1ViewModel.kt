package com.example.fluent.ui.screen1

import androidx.lifecycle.ViewModel
import com.example.fluent.data.WordDao
import com.example.fluent.data.Word
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class Screen1ViewModel @Inject constructor(
    private val repository: WordDao
) : ViewModel() {
    val items: Flow<List<Word>> = repository.getSampleData()
}