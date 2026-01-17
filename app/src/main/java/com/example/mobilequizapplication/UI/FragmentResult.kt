package com.example.mobilequizapplication.UI

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mobilequizapplication.R
import com.example.mobilequizapplication.databinding.FragmentResultBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentResult() : Fragment() {

    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val xpScore = arguments?.getString("param1")?.toIntOrNull() ?: 0
        val totalQuestions = arguments?.getString("param2")?.toIntOrNull() ?: 1
        val timeTaken = arguments?.getString("param3") ?: "00:00m"

        binding.tvTimer.text = timeTaken

        val pointsPerQuestion = 10
        val correctAnswers = xpScore / pointsPerQuestion

        val percentage = if (totalQuestions > 0) {
            (correctAnswers * 100) / totalQuestions
        } else {
            0
        }

        when {
            percentage >= 80 -> {
                binding.tvTitle.text = "Amazing Job!"
                binding.tvTitle.setTextColor(Color.parseColor("#2E7D32"))
            }
            percentage >= 50 -> {
                binding.tvTitle.text = "Good Effort!"
                binding.tvTitle.setTextColor(Color.parseColor("#1976D2"))
            }
            else -> {
                binding.tvTitle.text = "Don't Give Up!"
                binding.tvTitle.setTextColor(Color.parseColor("#D32F2F"))
            }
        }

        binding.tvScoreResult.text = "$correctAnswers / $totalQuestions"
        binding.progressResult.progress = percentage
        binding.tvPercent.text = "$percentage%"

        binding.btnBackHome.setOnClickListener {
            parentFragmentManager.popBackStack(null, androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE)
            parentFragmentManager.beginTransaction()
                .replace(R.id.main, FragmentCategories.newInstance())
                .commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(score: String, totalQuestions: String, timeTaken: String) =
            FragmentResult().apply {
                arguments = Bundle().apply {
                    putString("param1", score)
                    putString("param2", totalQuestions)
                    putString("param3", timeTaken)
                }
            }
    }
}