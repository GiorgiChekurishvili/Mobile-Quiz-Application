package com.example.mobilequizapplication.UI

import android.graphics.Color
import android.media.MediaPlayer
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

    private var mediaPlayer: MediaPlayer? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuizBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.timeLeft.collect { seconds ->
                binding.quizTimer.progress = seconds
                if (seconds < 5) {
                    binding.quizTimer.setIndicatorColor(Color.RED)
                } else {
                    binding.quizTimer.setIndicatorColor(Color.BLUE)
                }
            }
        }

        val categoryName = arguments?.getString("category_name") ?: "GeneralKnowledge"
        val difficultyName = arguments?.getString("difficulty") ?: "Medium"
        if (viewModel.questions.value.isEmpty()) {
            viewModel.loadQuestions(categoryName, difficultyName)
        }
        val category = try {
            Category.valueOf(categoryName)
        } catch (e: Exception) {
            Category.GeneralKnowledge
        }

        viewModel.loadQuestionsByCategory(category)


        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.questions.collectLatest { questions ->
                if (questions.isNotEmpty()) {
                    updateUI()
                    viewModel.startTimer()
                }
            }
        }


        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.currentIndex.collectLatest {
                updateUI()
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.score.collectLatest { currentScore ->
                binding.tvScore.text = "Score: $currentScore"
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            combine(viewModel.selectedAnswer, viewModel.isCorrect) { selected, isCorrect ->
                Pair(selected, isCorrect)
            }.collect { (selected, isCorrect) ->
                if (selected != null && isCorrect != null) {
                    handleAnswerFeedback(selected, isCorrect)
                }
            }
        }


        val buttons = listOf(binding.btnOption1, binding.btnOption2, binding.btnOption3, binding.btnOption4)
        buttons.forEach { button ->
            button.setOnClickListener {
                viewModel.submitAnswer(button.text.toString())
            }
        }
    }

    private fun handleAnswerFeedback(selected: String, isCorrect: Boolean) {
        val buttons = listOf(binding.btnOption1, binding.btnOption2, binding.btnOption3, binding.btnOption4)

        if (isCorrect) {
            playSound(R.raw.correct_sound)
        } else {
            playSound(R.raw.wrong_sound)
        }

        buttons.forEach { button ->
            button.isEnabled = false

            if (button.text == selected && button.visibility == View.VISIBLE) {
                if (isCorrect) {
                    button.setBackgroundColor(Color.GREEN)
                } else {
                    button.setBackgroundColor(Color.RED)
                }
            }
        }

        binding.root.postDelayed({
            resetButtonStyles()
            viewModel.moveToNext()
            viewModel.startTimer()
        }, 1000)
    }


    private fun playSound(resId: Int) {
        try {
            mediaPlayer?.stop()
            mediaPlayer?.release()
            mediaPlayer = MediaPlayer.create(requireContext(), resId)
            mediaPlayer?.setVolume(1.0f, 1.0f)
            mediaPlayer?.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
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
            val buttons = listOf(binding.btnOption1, binding.btnOption2, binding.btnOption3, binding.btnOption4)

            buttons.forEachIndexed { i, button ->
                val answer = answers.getOrNull(i)
                if (answer != null) {
                    button.text = answer
                    button.visibility = View.VISIBLE
                } else {
                    button.text = ""
                    button.visibility = View.GONE
                }
            }
        } else if (questions.isNotEmpty()) {
            navigateToResult()
        }
    }

    private fun navigateToResult() {
        if (!isAdded) return
        val finalScore = viewModel.score.value.toString()
        val totalQuestions = viewModel.questions.value.size.toString()
        val fragmentResult = FragmentResult.newInstance(finalScore, totalQuestions)
        val transaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.main, fragmentResult)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        mediaPlayer?.release()
        mediaPlayer = null
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(categoryName: String, difficulty: String) =
            FragmentQuiz().apply {
                arguments = Bundle().apply {
                    putString("category_name", categoryName)
                    putString("difficulty", difficulty)
                }
            }
    }
}