package com.example.androidstack.model

data class StackRequest(
    val query: String = "android",
    val sort: String = "creation",
    val order: String = "asc")