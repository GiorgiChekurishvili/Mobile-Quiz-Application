package com.example.mobilequizapplication.Data.Repository.Interface

import com.example.mobilequizapplication.Domain.Enum.Category
import com.example.mobilequizapplication.Domain.Enum.Difficulty
import com.example.mobilequizapplication.Domain.Enum.Type
import com.example.mobilequizapplication.Domain.Model.Question

interface IQuestionRepository {
    suspend fun getQuestions(amount: Int = 20) : List<Question>
    suspend fun getQuestionsByCategory(amount: Int = 20, category: Category): List<Question>
    suspend fun getQuestionsByDifficulty(amount: Int = 20, difficulty: Difficulty) : List<Question>
    suspend fun getQuestionsByType(amount: Int = 20, type: Type) : List<Question>
    suspend fun getQuestionsByDifficultyAndCategory(amount: Int = 20, category: Category, difficulty: Difficulty) : List<Question>
}