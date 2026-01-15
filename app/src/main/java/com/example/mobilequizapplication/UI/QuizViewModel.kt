package com.example.mobilequizapplication.UI

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobilequizapplication.Data.Repository.QuestionRepository
import com.example.mobilequizapplication.Domain.Enum.Category
import com.example.mobilequizapplication.Domain.Model.Question
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val repository: QuestionRepository // Injecting your friend's repository
) : ViewModel() {

    // Task 5: The list of questions fetched from API
    private val _questions = MutableStateFlow<List<Question>>(emptyList())
    val questions: StateFlow<List<Question>> = _questions

    // Task 6 & "Next Question": Tracker for the current question
    private val _currentIndex = MutableStateFlow(0)
    val currentIndex: StateFlow<Int> = _currentIndex

    // Task 1: The user's score
    private val _score = MutableStateFlow(0)
    val score: StateFlow<Int> = _score


    fun loadQuestionsByCategory(category: Category) {
        viewModelScope.launch {
            val fetchedQuestions = repository.getQuestionsByCategory(10, category)
            _questions.value = fetchedQuestions
        }
    }


    fun submitAnswer(selectedAnswer: String) {
        val currentQuestion = _questions.value.getOrNull(_currentIndex.value)

        if (currentQuestion != null) {

            if (selectedAnswer == currentQuestion.correctAnswer) {
                _score.value += 10
            }

            // Next Question functionality
            if (_currentIndex.value < _questions.value.size - 1) {
                _currentIndex.value += 1
            } else {
                // TODO: Handle Quiz Completed (Navigate to FragmentResult)
            }
        }
    }
}