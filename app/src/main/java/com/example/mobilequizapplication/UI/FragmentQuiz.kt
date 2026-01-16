package com.example.mobilequizapplication.UI

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.mobilequizapplication.Domain.Enum.Category
import com.example.mobilequizapplication.R
import com.example.mobilequizapplication.databinding.FragmentQuizBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FragmentQuiz : Fragment() {

    private var _binding: FragmentQuizBinding? = null
    private val binding get() = _binding!!

    private val viewModel: QuizViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuizBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // --- TIMER OBSERVATION ---
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.timeLeft.collect { seconds ->
                // Update the ProgressBar (assuming max is 30 in XML)
                binding.quizTimer.progress = seconds

                // Visual feedback: change color to red if time is low (under 5 seconds)
                if (seconds < 5) {
                    binding.quizTimer.setIndicatorColor(Color.RED)
                } else {
                    binding.quizTimer.setIndicatorColor(Color.BLUE)
                }
            }
        }

        val categoryName = arguments?.getString("category_name") ?: "GeneralKnowledge"
        val category = try {
            Category.valueOf(categoryName)
        } catch (e: Exception) {
            Category.GeneralKnowledge
        }

        viewModel.loadQuestionsByCategory(category)

        // Observe Questions and Start Timer when data arrives
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.questions.collectLatest { questions ->
                if (questions.isNotEmpty()) {
                    updateUI()
                    viewModel.startTimer() // Start timer for the first question
                }
            }
        }

        // Observe Index (Updates the text when moving to next question)
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.currentIndex.collectLatest {
                updateUI()
            }
        }

        // Task 1: Observe Score
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.score.collectLatest { currentScore ->
                binding.tvScore.text = "Score: $currentScore"
            }
        }

        // Visual Feedback Logic (Green/Red)
        viewLifecycleOwner.lifecycleScope.launch {
            combine(viewModel.selectedAnswer, viewModel.isCorrect) { selected, isCorrect ->
                Pair(selected, isCorrect)
            }.collect { (selected, isCorrect) ->
                if (selected != null && isCorrect != null) {
                    handleAnswerFeedback(selected, isCorrect)
                }
            }
        }

        // Setup Answer Buttons
        val buttons = listOf(binding.btnOption1, binding.btnOption2, binding.btnOption3, binding.btnOption4)
        buttons.forEach { button ->
            button.setOnClickListener {
                viewModel.submitAnswer(button.text.toString())
            }
        }
    }

    private fun handleAnswerFeedback(selected: String, isCorrect: Boolean) {
        val buttons = listOf(binding.btnOption1, binding.btnOption2, binding.btnOption3, binding.btnOption4)

        buttons.forEach { button ->
            button.isEnabled = false // Disable to prevent multiple clicks

            if (button.text == selected) {
                if (isCorrect) {
                    button.setBackgroundColor(Color.GREEN)
                } else {
                    button.setBackgroundColor(Color.RED)
                }
            }
        }

        // Wait 1 second, then reset and move to next question
        binding.root.postDelayed({
            resetButtonStyles()
            viewModel.moveToNext()
            viewModel.startTimer() // RESTART TIMER for the next question
        }, 1000)
    }

    private fun resetButtonStyles() {
        val buttons = listOf(binding.btnOption1, binding.btnOption2, binding.btnOption3, binding.btnOption4)
        buttons.forEach { button ->
            button.isEnabled = true
            button.setBackgroundColor(Color.TRANSPARENT)
        }
    }

    private fun updateUI() {
        val questions = viewModel.questions.value
        val index = viewModel.currentIndex.value

        if (index < questions.size) {
            val currentQuestion = questions[index]
            binding.tvQuestionText.text = currentQuestion.text

            val answers = currentQuestion.allAnswers.shuffled()
            binding.btnOption1.text = answers.getOrNull(0) ?: ""
            binding.btnOption2.text = answers.getOrNull(1) ?: ""
            binding.btnOption3.text = answers.getOrNull(2) ?: ""
            binding.btnOption4.text = answers.getOrNull(3) ?: ""
        } else if (questions.isNotEmpty()) {
            navigateToResult()
        }
    }

    private fun navigateToResult() {
        if (!isAdded) return
        val finalScore = viewModel.score.value.toString()
        val fragmentResult = FragmentResult.newInstance(finalScore, "")
        val transaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.main, fragmentResult)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(categoryName: String, param2: String) =
            FragmentQuiz().apply {
                arguments = Bundle().apply {
                    putString("category_name", categoryName)
                }
            }
    }
}