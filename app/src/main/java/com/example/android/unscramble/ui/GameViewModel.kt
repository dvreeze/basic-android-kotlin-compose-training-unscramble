package com.example.android.unscramble.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.*

class GameViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(GameUiState.make())

    init {
        resetGame()
    }

    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    fun resetGame() {
        _uiState.updateAndGet { it.resetGame() }
    }

    fun updateUserGuess(guessedWord: String) {
        _uiState.updateAndGet { it.updateUserGuess(guessedWord) }
    }

    fun checkUserGuess() {
        _uiState.updateAndGet { it.checkUserGuess() }
    }

    fun skipWord() {
        _uiState.updateAndGet { it.skipWord() }
    }
}
