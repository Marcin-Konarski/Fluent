package com.example.fluent

import com.example.fluent.data.Word

// this class defines events - user actions
sealed interface WordEvent {
    object saveWord: WordEvent
    data class setWord(val word: String): WordEvent
    data class setTranslation(val translation: String): WordEvent
    data class deleteWord(val word: Word): WordEvent
    object showScreen: WordEvent
    object hideScreen: WordEvent
}