package com.example.mobilequizapplication.UI

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mobilequizapplication.R
import com.example.mobilequizapplication.databinding.FragmentDifficultyBinding
import com.google.android.material.bottomnavigation.BottomNavigationView // Import BottomNavigationView

class DifficultyFragment : Fragment() {


    private var _binding: FragmentDifficultyBinding? = null
    private val binding get() = _binding!!

    private var categoryName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            categoryName = it.getString(ARG_CATEGORY_NAME)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDifficultyBinding.inflate(inflater, container, false)

        activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)?.visibility = View.GONE
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.tvTopic.text = "TOPIC: ${categoryName?.uppercase()}"


        binding.btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.btnEasy.setOnClickListener {
            navigateToQuiz(categoryName ?: "General", "easy")
        }

        binding.btnMedium.setOnClickListener {
            navigateToQuiz(categoryName ?: "General", "medium")
        }

        binding.btnHard.setOnClickListener {
            navigateToQuiz(categoryName ?: "General", "hard")
        }
    }

    private fun navigateToQuiz(categoryName: String, difficulty: String) {
        val fragmentQuiz = FragmentQuiz.newInstance(categoryName, difficulty)

        parentFragmentManager.beginTransaction()
            .replace(R.id.main, fragmentQuiz)
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)?.visibility = View.VISIBLE

        _binding = null
    }

    companion object {
        private const val ARG_CATEGORY_NAME = "arg_category_name"

        fun newInstance(categoryName: String): DifficultyFragment {
            return DifficultyFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_CATEGORY_NAME, categoryName)
                }
            }
        }
    }
}
