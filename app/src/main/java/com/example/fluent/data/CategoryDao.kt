package com.example.fluent.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Insert
    suspend fun insertCategory(category: Category): Long

    @Query("SELECT * FROM Category ORDER BY name ASC")
    fun getAllCategories(): Flow<List<Category>>

    @Query("SELECT id FROM Category WHERE name = :categoryName LIMIT 1")
    suspend fun getCategoryIdByName(categoryName: String): Int?

    @Delete
    suspend fun deleteCategory(category: Category)

    @Query("UPDATE word SET categoryId = :defaultCategoryId WHERE categoryId = :categoryId")
    suspend fun reassignWordsToDefaultCategory(categoryId: Int, defaultCategoryId: Int)

    @Transaction
    suspend fun deleteCategoryAndReassignWords(category: Category, defaultCategoryId: Int) {
        reassignWordsToDefaultCategory(category.id, defaultCategoryId)
        deleteCategory(category)
    }
}