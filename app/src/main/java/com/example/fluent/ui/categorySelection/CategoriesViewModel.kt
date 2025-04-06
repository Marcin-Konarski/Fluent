package com.example.fluent.ui.categorySelection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fluent.data.Category
import com.example.fluent.data.CategoryDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val repository: CategoryDao
) : ViewModel() {

    private val _uiState = MutableStateFlow(CategoryUiState())
    val uiState: StateFlow<CategoryUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getAllCategories().collect { categories ->
                _uiState.value = _uiState.value.copy(
                    categories = categories
                )
            }
        }
    }

    fun addCategory(name: String) {
        viewModelScope.launch {
            repository.insertCategory(Category(name = name))
        }
    }

    fun renameCategory(category: Category, newName: String) {
        viewModelScope.launch {
            // Create updated category with the same ID but new name
            val updatedCategory = category.copy(name = newName)
            repository.updateCategory(updatedCategory)
        }
    }

    fun deleteCategory(category: Category) {
        viewModelScope.launch {
            // If you want to reassign words when deleting a category
            // Uncomment this and provide a default category ID
            // repository.deleteCategoryAndReassignWords(category, defaultCategoryId = 1)

            // Or just delete the category without reassigning
            repository.deleteCategory(category)
        }
    }
}


data class CategoryUiState(
    val categories: List<Category> = emptyList(),
    val selectedCategoryId: Int? = null
)