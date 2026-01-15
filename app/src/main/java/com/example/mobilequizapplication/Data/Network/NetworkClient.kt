package com.example.mobilequizapplication.Data.Network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkClient {
    private  const val BASE_URL = "https://opentdb.com/"

    val apiService: IQuizApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(IQuizApi::class.java)
    }
}