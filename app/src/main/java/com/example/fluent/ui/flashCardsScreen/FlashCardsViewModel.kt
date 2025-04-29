package com.example.fluent.ui.flashCardsScreen

import androidx.lifecycle.SavedStateHandle
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
import kotlinx.coroutines.flow.update


@HiltViewModel
class FlashCardsViewModel @Inject constructor(
        private val wordDao: WordDao,
        private val categoryDao: CategoryDao,
        private val savedStateHandle: SavedStateHandle
) : ViewModel() {

        private val _selectedCategoryId = MutableStateFlow<Int?>(
                savedStateHandle.get<Int?>("selectedCategoryId")
        )
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

        // Initialize with sample data
        init {
                loadFlashCards()
        }

        fun selectCategory(categoryId: Int?) {
                if (_selectedCategoryId.value != categoryId) {
                        _selectedCategoryId.value = categoryId
                }
        }

        private fun loadFlashCards() {
                viewModelScope.launch {
                        wordList.collect { words ->
                                _cardsData.value = words
                        }
                }
        }

        // Card data state (now mutable list to support reordering)
        private val _cardsData = MutableStateFlow<List<Word>>(emptyList())
        val cardsData = _cardsData.asStateFlow()

        fun moveCardToBack(index: Int) {
                _cardsData.update { currentCards ->
                        if (index < 0 || index >= currentCards.size) return@update currentCards

                        // Create a new list with the card moved to the back
                        val mutableList = currentCards.toMutableList()
                        val cardToMove = mutableList.removeAt(index)
                        mutableList.add(cardToMove)
                        mutableList
                }
        }
}