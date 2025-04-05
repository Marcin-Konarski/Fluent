package com.example.fluent.ui.listWordsScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fluent.data.WordDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListWordsViewModel @Inject constructor(
    private val repository: WordDao
) : ViewModel() {
    private val _selectedCategory = MutableStateFlow("")
    val selectedCategory = _selectedCategory.asStateFlow()

    val categories = repository.getAllCategories()

    val wordList = _selectedCategory.flatMapLatest { category ->
        if(category.isEmpty()) {
            repository.getSampleData() // if there is no category selected then show all words
        } else {
            repository.getWordsByCategory(category)
        }
    }

    fun selectCategory(category: String) {
        _selectedCategory.value = category
    }

    init{
        viewModelScope.launch {
            categories.collect { categoryList ->
                if(_selectedCategory.value.isEmpty() && categoryList.isNotEmpty()){
                    // leave empty to show all categories
                }
            }
        }
    }
}