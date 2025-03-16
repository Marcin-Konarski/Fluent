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

    // Continuously emits new data when the database updates.
    // Does not need suspend since Flow is asynchronous.
    // FOR DISPLAYING ALL WORDS ON MAIN SCREEN AS A LIST
    @Query("SELECT * FROM Word ORDER BY word ASC") // ASC - Ascending order
    fun getSampleData(): Flow<List<Word>>

    @Query("SELECT * FROM Word WHERE ID = :id")
    fun getDetailData(id: Int): Flow<Word?>

    @Delete
    suspend fun deleteWord(word: Word)

    // One-time fetch: Fetches the list once when called.
    // Needs suspend since it runs in a coroutine.
    // IT'S ENOUGH TO FETCH IT ONCE SINCE AFTER THE SCREEN
    // CLAUSES THE VIEWMODEL IS DESTROYED THUS DATA CANNOT CHANGE DURING USAGE.
    @Query("SELECT * FROM Word")
    suspend fun getAllWords(): List<Word>
}

// suspend functions run in the coroutine
// functions that don't retrieve date (instead just do something) can be suspend