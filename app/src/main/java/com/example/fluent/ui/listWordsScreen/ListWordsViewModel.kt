package com.example.fluent.ui.listWordsScreen

import androidx.lifecycle.ViewModel
import com.example.fluent.data.WordDao
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ListWordsViewModel @Inject constructor(
    private val repository: WordDao
) : ViewModel() {
    val wordList = repository.getSampleData()
}