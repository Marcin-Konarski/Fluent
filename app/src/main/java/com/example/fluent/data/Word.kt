package com.example.fluent.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Word(
    val word: String,
    val translation: String,
    val category: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)