package com.example.fluent.ui.addWordScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fluent.WordEventForAddWord
import com.example.fluent.data.WordState
import com.example.fluent.data.CategoryDao
import com.example.fluent.data.Word
import com.example.fluent.data.WordDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AddWordViewModel @Inject constructor(
    private val wordDao: WordDao,
    private val categoryDao: CategoryDao
) : ViewModel() {

    private val _state = MutableStateFlow(WordState())
    val state: StateFlow<WordState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            categoryDao.getAllCategories().collect { categories ->
                _state.update { currentState ->
                    currentState.copy(allCategories = categories)
                }
            }
        }
    }

    fun onEvent(event: WordEventForAddWord) {
        when(event) {
            is WordEventForAddWord.SetWordAddWord -> {
                _state.update { it.copy(word = event.word) }
            }
            is WordEventForAddWord.SetTranslation -> {
                _state.update { it.copy(translation = event.translation) }
            }
            is WordEventForAddWord.SetCategory -> {
                val selectedCategory = _state.value.allCategories.find { it.name == event.category }
                _state.update {
                    it.copy(
                        category = event.category,
                        categoryId = selectedCategory?.id ?: 0
                    )
                }
            }
            is WordEventForAddWord.SaveWordAddWord -> {
                saveWord()
            }
        }
    }

    private fun saveWord() {
        val word = _state.value.word
        val translation = _state.value.translation
        val categoryId = _state.value.categoryId

        if (word.isBlank() || translation.isBlank() || categoryId == 0) {
            return
        }

        viewModelScope.launch {
            val newWord = Word(
                word = word,
                translation = translation,
                categoryId = categoryId
            )

            wordDao.insertWord(newWord)

            // Reset state after saving
            _state.update {
                it.copy(
                    word = "",
                    translation = ""
                )
            }
        }
    }
}