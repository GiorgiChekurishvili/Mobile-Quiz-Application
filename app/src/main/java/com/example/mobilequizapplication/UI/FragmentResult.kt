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
class FragmentResult : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val score = arguments?.getString("param1") ?: "0"


        val tvScoreResult = view.findViewById<TextView>(R.id.tvScoreResult)


        tvScoreResult.text = score


        val btnHome = view.findViewById<Button>(R.id.btnBackHome)
        btnHome.setOnClickListener {

            parentFragmentManager.popBackStack()
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentResult().apply {
                arguments = Bundle().apply {
                    putString("param1", param1)
                    putString("param2", param2)
                }
            }
    }
}