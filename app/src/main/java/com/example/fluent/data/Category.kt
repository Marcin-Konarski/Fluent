package com.example.fluent.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Category(
    val name: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
)