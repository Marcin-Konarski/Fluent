package com.example.fluent.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Word::class],
    version = 1
)
abstract class WordDatabase: RoomDatabase() {
    abstract val dao: WordDao
} // no need to implement this since it's abstract class and room will generate everything
