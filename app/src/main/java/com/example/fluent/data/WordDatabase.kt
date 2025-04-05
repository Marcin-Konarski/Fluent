package com.example.fluent.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Word::class, Category::class],
    version = 1
)
abstract class WordDatabase : RoomDatabase() {
    abstract fun wordDao(): WordDao
    abstract fun categoryDao(): CategoryDao
} // no need to implement this since it's abstract class and room will generate everything
