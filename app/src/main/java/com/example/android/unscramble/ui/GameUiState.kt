package com.example.android.unscramble.ui

import com.example.android.unscramble.data.MAX_NO_OF_WORDS
import com.example.android.unscramble.data.SCORE_INCREASE

// TODO Move this immutable data model (that functionally updates itself during the game) out of this package
// Not all of it, but most of it. Only partially filled in guesses really belong to the UI-specific model.
class GameUiState(
    val currentWordState: CurrentWordState,
    val currentWordCount: Int,
    val score: Int,
    val userGuess: String?,
    val justMadeGuess: Boolean
) {
    companion object {

        fun make(): GameUiState {
            return GameUiState(
                CurrentWordState.make(),
                currentWordCount = 1,
                score = 0,
                userGuess = null,
                justMadeGuess = false
            )
        }
    }

    private fun updateGameState(updatedScore: Int): GameUiState {
        return GameUiState(
            currentWordState = currentWordState.next(),
            currentWordCount = currentWordCount.inc(),
            score = updatedScore,
            userGuess = null,
            justMadeGuess = false
        )
    }

    fun nonNullUserGuess(): String = userGuess ?: ""

    fun isGameOver(): Boolean = currentWordState.usedWords.size > MAX_NO_OF_WORDS

    fun isGuessedWordCorrect(): Boolean {
        return userGuess != null && userGuess.equals(
            currentWordState.currentWord,
            ignoreCase = true
        )
    }

    fun isGuessedWordWrong(): Boolean {
        return userGuess != null && !userGuess.equals(
            currentWordState.currentWord,
            ignoreCase = true
        )
    }

    fun resetGame(): GameUiState {
        return make()
    }

    fun updateUserGuess(guessedWord: String?): GameUiState {
        return GameUiState(
            currentWordState = currentWordState,
            currentWordCount = currentWordCount,
            score = score,
            userGuess = guessedWord,
            justMadeGuess = false
        )
    }

    fun checkUserGuess(): GameUiState {
        return if (userGuess == null) {
            this
        } else
            if (isGuessedWordCorrect()) {
                updateGameState(score.plus(SCORE_INCREASE))
            } else {
                updateUserGuess(null).let {
                    GameUiState(
                        currentWordState = it.currentWordState,
                        currentWordCount = it.currentWordCount, // Not increment currentWordCount?
                        score = it.score,
                        userGuess = it.userGuess,
                        justMadeGuess = true
                    )
                }
            }
    }

    fun skipWord(): GameUiState {
        return updateGameState(score)
    }
}
