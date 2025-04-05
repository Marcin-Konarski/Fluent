package com.example.fluent

import com.example.fluent.data.Word

data class WordState(
    val word: String = "",
    val translation: String = "",
    val category: String = "",
    val allCategories: List<String> = emptyList() // To hold all existing categories
)