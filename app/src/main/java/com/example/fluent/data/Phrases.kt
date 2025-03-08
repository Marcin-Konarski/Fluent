package com.example.fluent.data
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Phrases(
    val word: String,
    val translation: String,
    val description: String?,
    @PrimaryKey val id: Int? = null
)
