package com.example.mobilequizapplication.UI

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobilequizapplication.Domain.Enum.Category
import com.example.mobilequizapplication.R

class FragmentHome : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val rvCategories = view.findViewById<RecyclerView>(R.id.categoriesRecyclerView)


        rvCategories.layoutManager = GridLayoutManager(requireContext(), 2)


        val categoryList = Category.entries


        val adapter = CategoriesAdapter(categoryList) { selectedCategory ->

            navigateToQuiz(selectedCategory)
        }


        rvCategories.adapter = adapter
    }

    private fun navigateToQuiz(category: Category) {

        val fragmentQuiz = FragmentQuiz.newInstance(category.name, "")

        parentFragmentManager.beginTransaction()
            .replace(R.id.main, fragmentQuiz)
            .addToBackStack(null)
            .commit()
    }

    companion object {
        @JvmStatic
        fun newInstance() = FragmentHome()
    }
}