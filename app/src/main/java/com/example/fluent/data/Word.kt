package com.example.fluent.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Word(
    val word: String,
    val translation: String,
    val categoryId: Int,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)

data class WordState(
    val word: String = "",
    val translation: String = "",
    val categoryId: Int = 0,
    val category: String = "",
    val allCategories: List<Category> = emptyList()
)