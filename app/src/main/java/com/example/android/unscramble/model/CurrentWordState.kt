package com.example.android.unscramble.model

class CurrentWordState private constructor(
    val allWords: Set<String>,
    val usedWords: Set<String>,
    val currentScrambledWord: String,
    val currentWord: String
) {
    companion object {

        fun make(allWords: Set<String>): CurrentWordState {
            return next(emptySet(), allWords)
        }

        fun next(usedWords: Set<String>, allWords: Set<String>): CurrentWordState {
            return pickRandomWordAndShuffle(usedWords, allWords)
        }

        private fun pickRandomWordAndShuffle(
            usedWords: Set<String>,
            allWords: Set<String>
        ): CurrentWordState {
            val currentWord = allWords.random()

            return if (usedWords.contains(currentWord)) {
                pickRandomWordAndShuffle(usedWords, allWords)
            } else {
                CurrentWordState(
                    allWords = allWords,
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
        return next(usedWords, allWords)
    }
}
