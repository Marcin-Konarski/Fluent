package com.example.fluent.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface WordDao {
    @Upsert
    suspend fun insertWord(word: Word)

    @Query("SELECT * FROM word ORDER BY word ASC")
    fun getAllWords(): Flow<List<Word>>

    @Query("SELECT * FROM word WHERE id = :id")
    fun getWordById(id: Int): Flow<Word?>

    @Delete
    suspend fun deleteWord(word: Word)

    // Continuously emits new data when the database updates.
    // Does not need suspend since Flow is asynchronous.
    // FOR DISPLAYING ALL WORDS ON MAIN SCREEN AS A LIST
    @Query("SELECT * FROM Word ORDER BY word ASC") // ASC - Ascending order
    fun getSampleData(): Flow<List<Word>>

    @Query("SELECT * FROM Word WHERE ID = :id")
    fun getDetailData(id: Int): Flow<Word?>

    @Query("SELECT * FROM word WHERE categoryId = :categoryId ORDER BY word ASC")
    fun getWordsByCategoryId(categoryId: Int): Flow<List<Word>>

    // Join to get word with its category name
    @Query("SELECT w.*, c.name as categoryName FROM word w INNER JOIN category c ON w.categoryId = c.id WHERE w.id = :wordId")
    fun getWordWithCategoryName(wordId: Int): Flow<WordWithCategoryName>

    // Define class for the join result
    data class WordWithCategoryName(
        val id: Int,
        val word: String,
        val translation: String,
        val categoryId: Int,
        val categoryName: String
    )

    // To delete all words inside the category while category is deleted
    @Query("DELETE FROM word WHERE categoryId = :categoryId")
    suspend fun deleteWordsByCategoryId(categoryId: Int)
}
