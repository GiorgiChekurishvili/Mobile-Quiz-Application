package com.example.mobilequizapplication.Domain.Model

import com.example.mobilequizapplication.Domain.Enum.Category
import com.example.mobilequizapplication.Domain.Enum.Difficulty
import com.example.mobilequizapplication.Domain.Enum.Type

data class Question(
    val id: String,
    val type: Type,
    val difficulty: Difficulty,
    val category: Category,
    val text: String,
    val correctAnswer: String,
    val allAnswers: List<String>
)