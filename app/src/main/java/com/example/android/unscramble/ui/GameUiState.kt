package com.example.android.unscramble.ui

import com.example.android.unscramble.data.MAX_NO_OF_WORDS
import com.example.android.unscramble.data.SCORE_INCREASE

// TODO Move this immutable data model (that functionally updates itself during the game) out of this package
class GameUiState(
    val currentWordState: CurrentWordState,
    val currentWordCount: Int,
    val score: Int,
    val userGuess: String?
) {
    companion object {

        fun make(): GameUiState {
            return GameUiState(
                CurrentWordState.make(),
                currentWordCount = 1,
                score = 0,
                userGuess = null
            )
        }
    }

    private fun updateGameState(updatedScore: Int): GameUiState {
        return GameUiState(
            currentWordState = currentWordState.next(),
            currentWordCount = currentWordCount.inc(),
            score = updatedScore,
            userGuess = null
        )
    }

    fun nonNullUserGuess(): String = userGuess ?: ""

    fun isGameOver(): Boolean = currentWordState.usedWords.size > MAX_NO_OF_WORDS

    fun isGuessedWordCorrectOrAbsent(): Boolean {
        return userGuess == null || userGuess.equals(
            currentWordState.currentWord,
            ignoreCase = true
        )
    }

    fun isGuessedWordWrong(): Boolean {
        return !isGuessedWordCorrectOrAbsent()
    }

    fun resetGame(): GameUiState {
        return make()
    }

    fun updateUserGuess(guessedWord: String?): GameUiState {
        return GameUiState(
            currentWordState = currentWordState,
            currentWordCount = currentWordCount,
            score = score,
            userGuess = guessedWord
        )
    }

    fun checkUserGuess(): GameUiState {
        return if (isGuessedWordCorrectOrAbsent()) {
            updateGameState(score.plus(SCORE_INCREASE))
        } else {
            updateUserGuess(null)
        }
    }

    fun skipWord(): GameUiState {
        return GameUiState(
            currentWordState = currentWordState.next(),
            currentWordCount = currentWordCount,
            score = score,
            userGuess = null
        )
    }
}
