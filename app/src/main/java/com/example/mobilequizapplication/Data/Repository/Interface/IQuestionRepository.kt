package com.example.mobilequizapplication.Data.Repository.Interface

import com.example.mobilequizapplication.Domain.Enum.Category
import com.example.mobilequizapplication.Domain.Enum.Difficulty
import com.example.mobilequizapplication.Domain.Enum.Type
import com.example.mobilequizapplication.Domain.Model.Question

interface IQuestionRepository {
    suspend fun getQuestions(amount: Int = 10) : List<Question>
    suspend fun getQuestionsByCategory(amount: Int = 10, category: Category): List<Question>
    suspend fun getQuestionsByDifficulty(amount: Int = 10, difficulty: Difficulty) : List<Question>
    suspend fun getQuestionsByType(amount: Int = 10, type: Type) : List<Question>
    suspend fun getQuestionsByDifficultyAndCategory(amount: Int = 10, category: Category, difficulty: Difficulty) : List<Question>
}