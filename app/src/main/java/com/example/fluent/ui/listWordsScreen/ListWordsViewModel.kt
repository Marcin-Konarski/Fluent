package com.example.fluent.ui.listWordsScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fluent.data.CategoryDao
import com.example.fluent.data.Word
import com.example.fluent.data.WordDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListWordsViewModel @Inject constructor(
    private val wordDao: WordDao,
    private val categoryDao: CategoryDao
) : ViewModel() {

    private val _selectedCategoryId = MutableStateFlow<Int?>(null)
    val selectedCategoryId: StateFlow<Int?> = _selectedCategoryId

    val categories = categoryDao.getAllCategories()

    val wordList = _selectedCategoryId.flatMapLatest { categoryId ->
        if (categoryId == null) {
            // Show all words when no category is selected
            wordDao.getAllWords()
        } else {
            // Show words for the selected category
            wordDao.getWordsByCategoryId(categoryId)
        }
    }

    fun selectCategory(categoryId: Int?) {
        _selectedCategoryId.value = categoryId
    }

    fun deleteWord(word: Word) {
        viewModelScope.launch {
            wordDao.deleteWord(word)
        }
    }
}