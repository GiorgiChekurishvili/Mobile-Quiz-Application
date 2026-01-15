package com.example.mobilequizapplication.Data.DTO

import com.example.mobilequizapplication.Domain.Enum.Category
import com.example.mobilequizapplication.Domain.Enum.Difficulty
import com.example.mobilequizapplication.Domain.Enum.Type
import com.google.gson.annotations.SerializedName

data class QuestionDto(
    val type: Type,
    val difficulty: Difficulty,
    val category: Category,
    val question: String,
    @SerializedName("correct_answer")
    val correctAnswer: String,
    @SerializedName("incorrect_answers")
    val incorrectAnswers: List<String>
)