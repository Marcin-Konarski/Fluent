package com.example.fluent.data

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

interface PhrasesRepository {

    suspend fun insertPhrase(phrase: Phrases)

    suspend fun deletePhrase(phrase: Phrases)

    suspend fun getPhraseById(id: Int): Phrases?

    fun getPhrases(): Flow<List<Phrases>>
}