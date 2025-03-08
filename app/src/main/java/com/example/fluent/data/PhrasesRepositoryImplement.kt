package com.example.fluent.data

import kotlinx.coroutines.flow.Flow

class PhrasesRepositoryImplement(
    private val dao: PhrasesDao
): PhrasesRepository {

    override suspend fun insertPhrase(phrase: Phrases) {
        dao.insertPhrase(phrase)
    }

    override suspend fun deletePhrase(phrase: Phrases) {
        dao.deletePhrase(phrase)
    }

    override suspend fun getPhraseById(id: Int): Phrases? {
        return dao.getPhraseById(id)
    }

    override fun getPhrases(): Flow<List<Phrases>> {
        return dao.getPhrases()
    }


}