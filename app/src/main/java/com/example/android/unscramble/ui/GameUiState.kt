package com.example.android.unscramble.ui

import com.example.android.unscramble.data.MAX_NO_OF_WORDS
import com.example.android.unscramble.data.SCORE_INCREASE
import com.example.android.unscramble.data.allWords
import com.example.android.unscramble.model.GameConfig
import com.example.android.unscramble.model.GameState

class GameUiState(
    val gameState: GameState,
    val partialUserGuess: String,
    val guessJustMade: Boolean
) {
    companion object {

        fun make(): GameUiState {
            return GameUiState(
                gameState = GameState.make(GameConfig(MAX_NO_OF_WORDS, SCORE_INCREASE), allWords),
                partialUserGuess = "",
                guessJustMade = false
            )
        }
    }

    fun isGameOver(): Boolean = gameState.shouldGameBeOver()

    fun isGameAboutToBeOver(): Boolean = gameState.shouldGameAboutToBeOver()

    fun isGuessedWordCorrect(): Boolean {
        return gameState.updateUserGuess(partialUserGuess).isGuessedWordCorrect()
    }

    fun isGuessedWordWrong(): Boolean {
        return gameState.updateUserGuess(partialUserGuess).isGuessedWordWrong()
    }

    fun resetGame(): GameUiState {
        return make()
    }

    fun updatePartialUserGuess(partiallyGuessedWord: String): GameUiState {
        return GameUiState(gameState, partiallyGuessedWord, false)
    }

    fun updateUserGuess(guessedWord: String?): GameUiState {
        return GameUiState(gameState.updateUserGuess(guessedWord), guessedWord ?: "", false)
    }

    fun checkUserGuess(): GameUiState {
        return if (gameState.userGuess == null) {
            this
        } else {
            val nextGameState: GameState = gameState.checkUserGuess()

            if (nextGameState.score > gameState.score) {
                // Correct guess
                GameUiState(nextGameState, "", false)
            } else {
                GameUiState(nextGameState, partialUserGuess, true)
            }
        }
    }

    fun skipWord(): GameUiState {
        return GameUiState(gameState.skipWord(), "", false)
    }
}
