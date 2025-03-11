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

    @Query("SELECT * FROM Word ORDER BY word ASC") // ASC - Ascending order
    fun getSampleData(): Flow<List<Word>>

    @Query("SELECT * FROM Word WHERE ID = :id")
    fun getDetailData(id: Int): Flow<Word?>

    @Delete
    suspend fun deleteWord(word: Word)
}

// suspend functions run in the coroutine
// functions that don't retrieve date (instead just do something) can be suspend