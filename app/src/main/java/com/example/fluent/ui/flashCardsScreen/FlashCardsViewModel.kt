package com.example.fluent.ui.flashCardsScreen

import androidx.lifecycle.ViewModel
import com.example.fluent.data.WordDao
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class FlashCardsViewModel @Inject constructor(
        private val wordDao: WordDao
) : ViewModel() {

}
