package com.example.mobilequizapplication.Data.Mapper

import android.text.Html
import com.example.mobilequizapplication.Data.DTO.QuestionDto
import com.example.mobilequizapplication.Domain.Model.Question
import java.util.UUID

fun QuestionDto.toDomain(): Question {
    val allAnswersList = (this.incorrectAnswers + this.correctAnswer)
        .map { decodeHtml(it) }
        .shuffled()

    return Question(
        id = UUID.randomUUID().toString(),
        text = decodeHtml(this.question),
        category = this.category,
        difficulty = this.difficulty,
        type = this.type,
        allAnswers = allAnswersList,
        correctAnswer = decodeHtml(this.correctAnswer),
    )
}

private fun decodeHtml(text: String): String {
    return Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY).toString()
}