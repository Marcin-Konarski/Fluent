package com.example.fluent

import com.example.fluent.data.Word

// this classes defines events - user actions
sealed interface WordEventForScreen2 {
    data class DeleteWord(val word: Word): WordEventForScreen2
}

sealed interface WordEventForScreen3 {
    object SaveWord: WordEventForScreen3
    data class SetWord(val word: String): WordEventForScreen3
    data class SetTranslation(val translation: String): WordEventForScreen3
}

sealed interface WordEventForScreen4and5 {
    data class SetWord(val word: String): WordEventForScreen4and5
    data class SetTranslation(val translation: String): WordEventForScreen4and5
    object NextWord: WordEventForScreen4and5
    data class SetWordInput(val wordInput: String): WordEventForScreen4and5
    object CheckAnswer: WordEventForScreen4and5
}
