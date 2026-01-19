package com.example.mobilequizapplication.UI

import android.os.CountDownTimer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobilequizapplication.Data.Repository.Interface.IQuestionRepository
import com.example.mobilequizapplication.Domain.Enum.Category
import com.example.mobilequizapplication.Domain.Enum.Difficulty
import com.example.mobilequizapplication.Domain.Model.Question
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val repository: IQuestionRepository
) : ViewModel() {

    private val _questions = MutableStateFlow<List<Question>>(emptyList())
    val questions: StateFlow<List<Question>> = _questions

    private val _currentIndex = MutableStateFlow(0)
    val currentIndex: StateFlow<Int> = _currentIndex

    private val _score = MutableStateFlow(0)
    val score: StateFlow<Int> = _score

    private val _selectedAnswer = MutableStateFlow<String?>(null)
    val selectedAnswer = _selectedAnswer.asStateFlow()

    private val _isCorrect = MutableStateFlow<Boolean?>(null)
    val isCorrect = _isCorrect.asStateFlow()

    private val _gameFinished = MutableStateFlow(false)
    val gameFinished: StateFlow<Boolean> = _gameFinished

    private var timer: CountDownTimer? = null
    private val _timeLeft = MutableStateFlow(20)
    val timeLeft: StateFlow<Int> = _timeLeft

    fun loadQuestions(categoryName: String, difficultyName: String) {
        viewModelScope.launch {
            val categoryEnum = Category.entries.find {
                it.name.equals(categoryName, ignoreCase = true)
            } ?: Category.GeneralKnowledge

            val difficultyEnum = Difficulty.entries.find {
                it.name.equals(difficultyName, ignoreCase = true)
            } ?: Difficulty.Medium

            val fetchedQuestions = repository.getQuestionsByDifficultyAndCategory(
                category = categoryEnum,
                difficulty = difficultyEnum
            )


            _questions.value = fetchedQuestions.take(10)

            _currentIndex.value = 0
            _score.value = 0
            _selectedAnswer.value = null
            _isCorrect.value = null
            _gameFinished.value = false
        }
    }

    fun submitAnswer(selectedAnswer: String) {
        if (_gameFinished.value) return

        val currentQuestion = _questions.value.getOrNull(_currentIndex.value)

        if (currentQuestion != null) {
            _selectedAnswer.value = selectedAnswer
            val correct = selectedAnswer == currentQuestion.correctAnswer
            _isCorrect.value = correct
            if (correct) {
                _score.value += 10
            }
        }
    }

    fun moveToNext() {
        timer?.cancel()
        _selectedAnswer.value = null
        _isCorrect.value = null

        val currentListSize = _questions.value.size


        if (currentListSize == 0) return


        if (_currentIndex.value >= currentListSize - 1) {
            _gameFinished.value = true
        } else {
            _currentIndex.value += 1
        }
    }

    fun startTimer() {
        timer?.cancel()

        if (_gameFinished.value || _questions.value.isEmpty()) return


        if (_currentIndex.value >= _questions.value.size) return

        timer = object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                _timeLeft.value = (millisUntilFinished / 1000).toInt()
            }

            override fun onFinish() {
                _timeLeft.value = 0
                submitAnswer("")
            }
        }.start()
    }

    override fun onCleared() {
        super.onCleared()
        timer?.cancel()
    }
}