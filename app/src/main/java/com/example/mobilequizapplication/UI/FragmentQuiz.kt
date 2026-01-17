package com.example.mobilequizapplication.UI

import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.mobilequizapplication.R
import com.example.mobilequizapplication.databinding.FragmentQuizBinding
import com.google.android.material.card.MaterialCardView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FragmentQuiz : Fragment() {

    private var _binding: FragmentQuizBinding? = null
    private val binding get() = _binding!!

    private val viewModel: QuizViewModel by viewModels()

    private var mediaPlayer: MediaPlayer? = null

    data class OptionView(
        val card: MaterialCardView,
        val text: TextView,
        val defaultColor: Int
    )

    private lateinit var optionViews: List<OptionView>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuizBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        optionViews = listOf(
            OptionView(binding.btnOption1, binding.tvOption1, Color.parseColor("#FF80AB")), // Vibrant Pink
            OptionView(binding.btnOption2, binding.tvOption2, Color.parseColor("#80D8FF")), // Vibrant Blue
            OptionView(binding.btnOption3, binding.tvOption3, Color.parseColor("#B9F6CA")), // Vibrant Green
            OptionView(binding.btnOption4, binding.tvOption4, Color.parseColor("#FFFF8D"))  // Vibrant Yellow
        )

        optionViews.forEach { option ->
            option.card.setOnClickListener {
                viewModel.submitAnswer(option.text.text.toString())
            }
        }

        val categoryName = arguments?.getString("category_name") ?: "General"
        val difficultyName = arguments?.getString("difficulty") ?: "Medium"

        binding.tvCategoryName.text = categoryName

        if (viewModel.questions.value.isEmpty()) {
            viewModel.loadQuestions(categoryName, difficultyName)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.timeLeft.collect { seconds ->
                binding.quizTimer.progress = seconds
                binding.tvTimer.text = "00:${seconds.toString().padStart(2, '0')}"

                if (seconds < 5) {
                    binding.quizTimer.setIndicatorColor(Color.RED)
                    binding.tvTimer.setTextColor(Color.RED)
                } else {
                    binding.quizTimer.setIndicatorColor(Color.parseColor("#FF9800")) // Orange
                    binding.tvTimer.setTextColor(Color.parseColor("#2D2D2D"))
                }
            }
        }

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
    }

    private fun handleAnswerFeedback(selected: String, isCorrect: Boolean) {
        if (isCorrect) {
            playSound(R.raw.correct_sound)
        } else {
            playSound(R.raw.wrong_sound)
        }

        optionViews.forEach { option ->
            option.card.isEnabled = false

            if (option.text.text == selected) {
                if (isCorrect) {
                    option.card.setCardBackgroundColor(Color.parseColor("#2E7D32"))
                } else {
                    option.card.setCardBackgroundColor(Color.parseColor("#C62828"))
                }
                option.text.setTextColor(Color.WHITE)

            } else {
                option.card.setCardBackgroundColor(Color.parseColor("#EEEEEE"))
                option.text.setTextColor(Color.parseColor("#BDBDBD"))
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            delay(1000)
            resetButtonStyles()
            viewModel.moveToNext()
            viewModel.startTimer()
        }
    }

    private fun resetButtonStyles() {
        optionViews.forEach { option ->
            option.card.isEnabled = true

            option.card.setCardBackgroundColor(option.defaultColor)

            option.text.setTextColor(Color.parseColor("#2D2D2D"))
        }
    }

    private fun updateUI() {
        resetButtonStyles()

        val questions = viewModel.questions.value
        val index = viewModel.currentIndex.value

        if (index < questions.size) {
            val currentQuestion = questions[index]

            binding.tvQuestionText.text = currentQuestion.text

            binding.tvQuestionCount.text = "QUESTION ${index + 1} OF ${questions.size}"

            val answers = currentQuestion.allAnswers.shuffled()

            optionViews.forEachIndexed { i, option ->
                val answer = answers.getOrNull(i)
                if (answer != null) {
                    option.text.text = answer
                    option.card.visibility = View.VISIBLE
                    option.card.isEnabled = true
                } else {
                    option.text.text = ""
                    option.card.visibility = View.GONE
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