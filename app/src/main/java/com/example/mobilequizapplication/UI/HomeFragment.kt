package com.example.mobilequizapplication.UI

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.mobilequizapplication.Domain.Enum.Category
import com.example.mobilequizapplication.Domain.Enum.Difficulty
import com.example.mobilequizapplication.R
import com.example.mobilequizapplication.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setupTopTopics()


        Glide.with(this)
            .load("https://api.dicebear.com/7.x/initials/png?seed=Explorer")
            .into(binding.ivProfile)


        binding.tvViewAll.setOnClickListener {
            navigateToTopics()
        }


        binding.btnPlayNow.setOnClickListener {

            val randomCategory = Category.entries.random()
            val randomDifficulty = Difficulty.entries.random()


            navigateToQuiz(randomCategory, randomDifficulty)
        }
    }


    private fun setupTopTopics() {

        val randomCategories = Category.entries.shuffled().take(4)


        val topicBindings = listOf(
            binding.includeTopic1,
            binding.includeTopic2,
            binding.includeTopic3,
            binding.includeTopic4
        )


        randomCategories.forEachIndexed { index, category ->

            val currentTopicBinding = topicBindings[index]


            currentTopicBinding.tvCategoryName.text = category.displayName


            Glide.with(this)
                .load("https://api.dicebear.com/7.x/bottts/png?seed=${category.displayName}")
                .into(currentTopicBinding.ivCategoryImage)


            currentTopicBinding.root.setOnClickListener {
                navigateToDifficultySelection(category)
            }
        }
    }


    private fun navigateToTopics() {
        val categoriesFragment = FragmentCategories.newInstance()

        parentFragmentManager.beginTransaction()
            .replace(R.id.main, categoriesFragment)
            .addToBackStack(null)
            .commit()
    }


    private fun navigateToDifficultySelection(category: Category) {
        val difficultyFragment = DifficultyFragment.newInstance(category.displayName)

        parentFragmentManager.beginTransaction()
            .replace(R.id.main, difficultyFragment)
            .addToBackStack(null)
            .commit()
    }


    private fun navigateToQuiz(category: Category, difficulty: Difficulty) {
        val quizFragment = FragmentQuiz.newInstance(category.displayName, difficulty.name.lowercase())

        parentFragmentManager.beginTransaction()
            .replace(R.id.main, quizFragment)
            .addToBackStack(null)
            .commit()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }
}
