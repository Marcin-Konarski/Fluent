package com.example.fluent.ui.screen4

import androidx.lifecycle.ViewModel
import com.example.fluent.data.WordDao
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class Screen4ViewModel @Inject constructor(
    private val repository: WordDao
): ViewModel() {

}