package com.example.mobilequizapplication.UI

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.mobilequizapplication.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentResult() : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val finalScore = arguments?.getString("param1") ?: "0"
        val totalQuestions = (arguments?.getString("param2")?.toIntOrNull() ?: 0) * 10


        val tvScoreResult = view.findViewById<TextView>(R.id.tvScoreResult)


        tvScoreResult.text = "Your Score: $finalScore/$totalQuestions"

        val btnHome = view.findViewById<Button>(R.id.btnBackHome)
        btnHome.setOnClickListener {

            parentFragmentManager.popBackStack(null, androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE)

            parentFragmentManager.beginTransaction()
                .replace(R.id.main, FragmentCategories.newInstance())
                .commit()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(score: String, param2: String) =
            FragmentResult().apply {
                arguments = Bundle().apply {
                    putString("param1", score)
                    putString("param2", param2)
                }
            }
    }
}