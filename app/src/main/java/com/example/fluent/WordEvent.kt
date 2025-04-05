package com.example.fluent

import com.example.fluent.data.Word

// this classes defines events - user actions
sealed interface WordEventForDeleteWord {
    data class DeleteWord(val word: Word): WordEventForDeleteWord
}

sealed interface WordEventForAddWord {
    object SaveWordAddWord: WordEventForAddWord
    data class SetWordAddWord(val word: String): WordEventForAddWord
    data class SetTranslation(val translation: String): WordEventForAddWord
    data class SetCategory(val category: String): WordEventForAddWord
}

sealed interface WordEventForLearnWordsScreen {
    data class SetWordLearnWords(val word: String): WordEventForLearnWordsScreen
    data class SetTranslation(val translation: String): WordEventForLearnWordsScreen
    object NextWordLearnWords: WordEventForLearnWordsScreen
    data class SetWordInputLearnWords(val wordInput: String): WordEventForLearnWordsScreen
    object CheckAnswer: WordEventForLearnWordsScreen
}
