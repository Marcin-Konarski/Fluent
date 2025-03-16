package com.example.fluent.ui.screen6

import androidx.lifecycle.ViewModel
import com.example.fluent.data.WordDao
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class Screen6ViewModel @Inject constructor(
        private val wordDao: WordDao
) : ViewModel() {

}
