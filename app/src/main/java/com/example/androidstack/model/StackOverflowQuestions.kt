package com.example.androidstack.model

import com.google.gson.annotations.SerializedName

data class StackOverflowQuestions(
    @SerializedName("items")
    val questions: List<Question> = emptyList()
)