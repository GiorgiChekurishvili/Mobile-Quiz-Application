package com.example.mobilequizapplication.Data.Repository

import com.example.mobilequizapplication.Data.Repository.Interface.IQuestionRepository
import com.example.mobilequizapplication.Domain.Enum.Category
import com.example.mobilequizapplication.Domain.Enum.Difficulty
import com.example.mobilequizapplication.Domain.Enum.Type
import com.example.mobilequizapplication.Domain.Model.Question
import com.example.mobilequizapplication.Data.Mapper.toDomain
import com.example.mobilequizapplication.Data.Network.IQuizApi
import javax.inject.Inject
import kotlin.collections.map

class QuestionRepository @Inject constructor(private  val apiService: IQuizApi) :
    IQuestionRepository {
    override suspend fun getQuestions(amount: Int): List<Question> {
        return fetchQuestions(amount = amount)
    }

    override suspend fun getQuestionsByCategory(
        amount: Int,
        category: Category
    ): List<Question> {
        return fetchQuestions(amount = amount, categoryId = category.id)
    }

    override suspend fun getQuestionsByDifficulty(
        amount: Int,
        difficulty: Difficulty
    ): List<Question> {
        return fetchQuestions(amount = amount, difficultyStr = difficulty.apiValue)
    }

    override suspend fun getQuestionsByType(
        amount: Int,
        type: Type
    ): List<Question> {
        return fetchQuestions(amount = amount, typeStr = type.apiValue)
    }

    private  suspend fun fetchQuestions(
        amount: Int,
        categoryId: Int? = null,
        difficultyStr: String? = null,
        typeStr: String? = null
    ) : List<Question>{
        return  try{
            val response = apiService.getQuestions(
                amount = amount,
                category = categoryId,
                difficulty = difficultyStr,
                type = typeStr
            )
            if(response.responseCode == 0){
                response.results.map { it.toDomain() }
            }
            else{
                emptyList()
            }
        }
        catch (e : Exception){
            e.printStackTrace()
            emptyList()
        }
    }
}