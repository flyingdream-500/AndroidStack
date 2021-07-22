package com.example.androidstack.model

import androidx.lifecycle.LiveData
import androidx.paging.PagedList

data class StackResponse(
    val data: LiveData<PagedList<Question>>,
    val networkErrors: LiveData<String>) {
}