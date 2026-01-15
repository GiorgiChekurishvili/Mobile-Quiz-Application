package com.example.mobilequizapplication.DI

import com.example.mobilequizapplication.Data.Network.NetworkClient
import com.example.mobilequizapplication.Data.Repository.Interface.IQuestionRepository
import com.example.mobilequizapplication.Data.Repository.QuestionRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.example.mobilequizapplication.Data.Network.IQuizApi
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class QuizModule {
    @Binds
    @Singleton
    abstract fun bindQuizRepository(
        impl: QuestionRepository
    ): IQuestionRepository

    companion object {

        @Provides
        @Singleton
        fun provideApiService(): IQuizApi {
            return NetworkClient.apiService
        }
    }
}