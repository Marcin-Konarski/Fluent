package com.example.fluent.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database( // Declaration of all tables, the only one rn is Phrases obv
    entities = [Phrases::class],
    version = 1 // in case database changes
)
abstract class PhrasesDatabse: RoomDatabase() {

    abstract val dao: PhrasesDao
}