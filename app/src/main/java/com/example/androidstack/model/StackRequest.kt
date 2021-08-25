package com.example.androidstack.model

import com.example.androidstack.util.DEFAULT_SEARCH
import com.example.androidstack.util.ORDER_ASC
import com.example.androidstack.util.SORT_CREATION

data class StackRequest(
    val query: String = DEFAULT_SEARCH,
    val sort: String = SORT_CREATION,
    val order: String = ORDER_ASC)