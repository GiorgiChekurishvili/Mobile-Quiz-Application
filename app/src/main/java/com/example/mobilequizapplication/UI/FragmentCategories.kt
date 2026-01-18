package com.example.mobilequizapplication.UI

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mobilequizapplication.Adapter.CategoryTopicAdapter
import com.example.mobilequizapplication.Domain.Enum.Category
import com.example.mobilequizapplication.R
import com.example.mobilequizapplication.databinding.FragmentCategoriesBinding

class FragmentCategories : Fragment() {

    private var _binding: FragmentCategoriesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvCategories.layoutManager = GridLayoutManager(requireContext(), 2)

        val categoryList = Category.entries


        val adapter = CategoryTopicAdapter(categoryList) { selectedCategory ->
            navigateToDifficultySelection(selectedCategory)
        }
        binding.rvCategories.adapter = adapter
    }

    private fun navigateToDifficultySelection(category: Category) {
        val difficultyFragment = DifficultyFragment.newInstance(category.displayName)

        parentFragmentManager.beginTransaction()
            .replace(R.id.main, difficultyFragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = FragmentCategories()
    }
}
