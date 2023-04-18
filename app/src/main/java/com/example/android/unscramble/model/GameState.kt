package com.example.android.unscramble.model

class GameState(
    val gameConfig: GameConfig,
    val currentWordState: CurrentWordState,
    val currentWordCount: Int,
    val score: Int,
    val userGuess: String?
) {
    companion object {

        fun make(gameConfig: GameConfig, allWords: Set<String>): GameState {
            return GameState(
                gameConfig = gameConfig,
                currentWordState = CurrentWordState.make(allWords),
                currentWordCount = 1,
                score = 0,
                userGuess = null
            )
        }
    }

    private fun updateScoreAndProceed(updatedScore: Int): GameState {
        return GameState(
            gameConfig = gameConfig,
            currentWordState = currentWordState.next(),
            currentWordCount = currentWordCount.inc(),
            score = updatedScore,
            userGuess = null
        )
    }

    fun shouldGameAboutToBeOver(): Boolean =
        currentWordState.usedWords.size == gameConfig.maxNumberOfWords

    fun shouldGameBeOver(): Boolean = currentWordState.usedWords.size > gameConfig.maxNumberOfWords

    fun noGuessMade(): Boolean = userGuess == null

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

    fun resetGame(): GameState {
        return make(gameConfig, currentWordState.allWords)
    }

    /**
     * Updates a user guess. It is not checked whether the game has ended.
     */
    fun updateUserGuess(guessedWord: String?): GameState {
        return GameState(
            gameConfig = gameConfig,
            currentWordState = currentWordState,
            currentWordCount = currentWordCount,
            score = score,
            userGuess = guessedWord
        )
    }

    /**
     * Check the user guess, if any. If the resulting GameState has a higher score, the guess was correct.
     * It is not checked whether the game has ended.
     */
    fun checkUserGuess(): GameState {
        return if (userGuess == null) {
            this
        } else
            if (isGuessedWordCorrect()) {
                updateScoreAndProceed(score.plus(gameConfig.scoreIncrease))
            } else {
                // Not increment currentWordCount?
                updateUserGuess(null)
            }
    }

    /**
     * Skips a word. It is not checked whether the game has ended.
     */
    fun skipWord(): GameState {
        return updateScoreAndProceed(score)
    }
}
