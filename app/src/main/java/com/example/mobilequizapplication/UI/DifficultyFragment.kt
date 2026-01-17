package com.example.mobilequizapplication.UI

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.mobilequizapplication.R

class DifficultyFragment : Fragment(R.layout.fragment_difficulty) {
    companion object {
        private const val ARG_CATEGORY_NAME = "arg_category_name"

        fun newInstance(categoryName: String): DifficultyFragment {
            val fragment = DifficultyFragment()
            val args = Bundle()
            args.putString(ARG_CATEGORY_NAME, categoryName)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val categoryName = arguments?.getString(ARG_CATEGORY_NAME) ?: "General"

        val tvTopic: TextView = view.findViewById(R.id.tvTopic)
        val btnBack: ImageButton = view.findViewById(R.id.btnBack)

        tvTopic.text = "TOPIC: ${categoryName.uppercase()}"

        val btnEasy: ConstraintLayout = view.findViewById(R.id.btnEasy)
        val btnMedium: ConstraintLayout = view.findViewById(R.id.btnMedium)
        val btnHard: ConstraintLayout = view.findViewById(R.id.btnHard)

        btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        btnEasy.setOnClickListener {
            navigateToQuiz(categoryName, "hard")
        }

        btnMedium.setOnClickListener {
            navigateToQuiz(categoryName, "hard")
        }

        btnHard.setOnClickListener {
            navigateToQuiz(categoryName, "hard")
        }
    }

    private fun navigateToQuiz(categoryName: String, difficulty: String) {
        val fragmentQuiz = FragmentQuiz.newInstance(categoryName, difficulty)

        parentFragmentManager.beginTransaction()
            .replace(R.id.main, fragmentQuiz)
            .addToBackStack(null)
            .commit()
    }
}