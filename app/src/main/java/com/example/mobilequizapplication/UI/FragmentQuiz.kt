package com.example.mobilequizapplication.UI

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


        val categoryName = arguments?.getString("category_name") ?: "GeneralKnowledge"
        val category = try {
            Category.valueOf(categoryName)
        } catch (e: Exception) {

            Category.GeneralKnowledge
        }

        viewModel.loadQuestionsByCategory(category)


        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.questions.collectLatest { questions ->
                if (questions.isNotEmpty()) updateUI()
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


        val buttons = listOf(binding.btnOption1, binding.btnOption2, binding.btnOption3, binding.btnOption4)
        buttons.forEach { button ->
            button.setOnClickListener {
                viewModel.submitAnswer(button.text.toString())
            }
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