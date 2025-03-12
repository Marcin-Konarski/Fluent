package com.example.fluent

import com.example.fluent.data.Word

// this class defines events - user actions
sealed interface WordEventForScreen2 {
    data class deleteWord(val word: Word): WordEventForScreen2
}

sealed interface WordEventForScreen3 {
    object saveWord: WordEventForScreen3
    data class setWord(val word: String): WordEventForScreen3
    data class setTranslation(val translation: String): WordEventForScreen3
}
