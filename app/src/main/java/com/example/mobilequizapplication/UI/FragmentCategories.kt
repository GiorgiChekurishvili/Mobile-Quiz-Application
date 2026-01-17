package com.example.mobilequizapplication.UI

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobilequizapplication.Domain.Enum.Category
import com.example.mobilequizapplication.R

class FragmentCategories : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_categories, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rvCategories = view.findViewById<RecyclerView>(R.id.rvCategories)

        // 1. Set Layout Manager to Grid (2 columns)
        rvCategories.layoutManager = GridLayoutManager(requireContext(), 2)

        // 2. Prepare Data
        val categoryList = Category.entries

        // 3. Set Adapter
        val adapter = CategoriesAdapter(categoryList) { selectedCategory ->
            navigateToQuiz(selectedCategory)
        }

        rvCategories.adapter = adapter
    }

    private fun navigateToQuiz(category: Category) {
        // Ensure FragmentQuiz exists and accepts these params
        val fragmentQuiz = FragmentQuiz.newInstance(category.name, "")

        parentFragmentManager.beginTransaction()
            .replace(R.id.main, fragmentQuiz)
            .addToBackStack(null)
            .commit()
    }

    companion object {
        @JvmStatic
        fun newInstance() = FragmentCategories()
    }
}