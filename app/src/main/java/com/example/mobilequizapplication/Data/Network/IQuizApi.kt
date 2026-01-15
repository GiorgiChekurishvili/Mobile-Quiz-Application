package com.example.mobilequizapplication.Data.Network

import com.example.mobilequizapplication.Data.DTO.QuizResponseDto
import retrofit2.http.GET
import retrofit2.http.Query


interface IQuizApi {
    @GET("api.php")
    suspend fun getQuestions(
        @Query("amount") amount: Int = 50,
        @Query("category") category: Int? = null,
        @Query("difficulty") difficulty: String? = null,
        @Query("type") type: String? = null
    ) : QuizResponseDto
}
