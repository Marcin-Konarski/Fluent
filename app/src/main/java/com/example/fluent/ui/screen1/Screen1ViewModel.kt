package com.example.fluent.ui.screen1

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fluent.WordEvent
import com.example.fluent.WordState
import com.example.fluent.data.WordDao
import com.example.fluent.data.Word
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class Screen1ViewModel @Inject constructor(
    private val repository: WordDao
) : ViewModel() {
    val items = repository.getSampleData()
}