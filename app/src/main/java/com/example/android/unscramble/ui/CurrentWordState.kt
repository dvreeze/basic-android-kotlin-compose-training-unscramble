package com.example.android.unscramble.ui

import com.example.android.unscramble.data.allWords

class CurrentWordState private constructor(
    val usedWords: Set<String>,
    val currentScrambledWord: String,
    val currentWord: String
) {
    companion object {

        fun make(): CurrentWordState {
            return next(emptySet())
        }

        fun next(usedWords: Set<String>): CurrentWordState {
            return pickRandomWordAndShuffle(usedWords)
        }

        private fun pickRandomWordAndShuffle(usedWords: Set<String>): CurrentWordState {
            val currentWord = allWords.random()

            return if (usedWords.contains(currentWord)) {
                pickRandomWordAndShuffle(usedWords)
            } else {
                CurrentWordState(
                    usedWords = usedWords.plus(currentWord),
                    currentScrambledWord = shuffleWord(currentWord),
                    currentWord = currentWord
                )
            }
        }

        private fun shuffleWord(word: String): String {
            val tempWord = word.toCharArray()
            tempWord.shuffle()
            while (String(tempWord) == word) {
                tempWord.shuffle()
            }
            return String(tempWord)
        }
    }

    fun next(): CurrentWordState {
        return next(usedWords)
    }
}
