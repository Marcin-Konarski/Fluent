package com.example.fluent.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PhrasesDao { //Data Access Object

    //if there is already object with this id we will replace old one with new one
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPhrase(phrase: Phrases)

    @Delete
    suspend fun deletePhrase(phrase: Phrases)

    @Query("SELECT * FROM Phrases WHERE id = :id")
    suspend fun getPhraseById(id: Int): Phrases?

    @Query("SELECT * FROM Phrases")
    fun getPhrases(): Flow<List<Phrases>>
}