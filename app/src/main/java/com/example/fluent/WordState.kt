package com.example.fluent

import com.example.fluent.data.Word

data class WordState(
    val words: List<Word> = emptyList(),
    val word: String = "",
    val translation: String = "",
    val id: Int = 0,
)