package com.example.mobilequizapplication.Data.DTO

import com.google.gson.annotations.SerializedName

data class QuizResponseDto (
    @SerializedName("response_code")
    val responseCode: Int,
    val results: List<QuestionDto>
)