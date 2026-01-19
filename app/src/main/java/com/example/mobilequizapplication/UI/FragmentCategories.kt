package com.example.mobilequizapplication.UI

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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


    private lateinit var adapter: CategoryTopicAdapter
    private val fullCategoryList = Category.entries

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupSearch()
    }

    private fun setupRecyclerView() {
        binding.rvCategories.layoutManager = GridLayoutManager(requireContext(), 2)


        adapter = CategoryTopicAdapter(fullCategoryList) { selectedCategory ->
            navigateToDifficultySelection(selectedCategory)
        }
        binding.rvCategories.adapter = adapter
    }

    private fun setupSearch() {

        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                filterCategories(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun filterCategories(query: String) {
        val searchText = query.lowercase().trim()

        val filteredList = if (searchText.isEmpty()) {
            fullCategoryList
        } else {

            fullCategoryList.filter {
                it.displayName.lowercase().contains(searchText)
            }
        }


        adapter.updateList(filteredList)
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