package com.example.fluent.ui.detailsScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.fluent.WordEventForDeleteWord
import com.example.fluent.data.Category
import com.example.fluent.data.CategoryDao
import com.example.fluent.data.WordDao
import com.example.fluent.data.Word
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val repository: WordDao,
    private val categoryDao: CategoryDao,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val itemId: Int = checkNotNull(savedStateHandle["itemId"])
    val item: StateFlow<Word?> = repository.getDetailData(itemId)
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    val categories: StateFlow<List<Category>> = categoryDao.getAllCategories()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun onEvent(event: WordEventForDeleteWord){
        when(event){
            is WordEventForDeleteWord.DeleteWord -> {
                viewModelScope.launch {
                    repository.deleteWord(event.word)
                }
            }
        }
    }


    fun editWord(word: String, translation: String, categoryId: Int) {
        viewModelScope.launch {
            item.value?.let {
                val updated = it.copy(
                    word = word,
                    translation = translation,
                    categoryId = categoryId
                )
                repository.insertWord(updated)
            }
        }
    }
}