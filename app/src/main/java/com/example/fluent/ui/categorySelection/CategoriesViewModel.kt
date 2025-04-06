package com.example.fluent.ui.categorySelection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fluent.data.Category
import com.example.fluent.data.CategoryDao
import com.example.fluent.data.WordDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val categoryDoa: CategoryDao,
    private val wordDao: WordDao
) : ViewModel() {

    private val _uiState = MutableStateFlow(CategoryUiState())
    val uiState: StateFlow<CategoryUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            categoryDoa.getAllCategories().collect { categories ->
                _uiState.value = _uiState.value.copy(
                    categories = categories
                )
            }
        }
    }

    fun addCategory(name: String) {
        viewModelScope.launch {
            categoryDoa.insertCategory(Category(name = name))
        }
    }

    fun renameCategory(category: Category, newName: String) {
        viewModelScope.launch {
            // Create updated category with the same ID but new name
            val updatedCategory = category.copy(name = newName)
            categoryDoa.updateCategory(updatedCategory)
        }
    }

    fun deleteCategory(category: Category) {
        viewModelScope.launch {
            // repository.deleteCategoryAndReassignWords(category, defaultCategoryId = 1)
            categoryDoa.deleteCategory(category)
        }
    }

    fun deleteCategoryAndWords(category: Category) {
        viewModelScope.launch {
            wordDao.deleteWordsByCategoryId(category.id)
            categoryDoa.deleteCategory(category)
        }
    }
}


data class CategoryUiState(
    val categories: List<Category> = emptyList(),
    val selectedCategoryId: Int? = null
)