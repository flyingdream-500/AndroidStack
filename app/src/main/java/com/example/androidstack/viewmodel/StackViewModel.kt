package com.example.androidstack.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.example.androidstack.App
import com.example.androidstack.api.StackService
import com.example.androidstack.db.StackCache
import com.example.androidstack.model.Question
import com.example.androidstack.model.StackRequest
import com.example.androidstack.model.StackResponse
import com.example.androidstack.repository.StackRepository
import java.util.concurrent.Executors

class StackViewModel(private val repository: StackRepository): ViewModel() {

    private val queryLiveData = MutableLiveData<StackRequest>()
    private val repoResult: LiveData<StackResponse> = Transformations.map(queryLiveData) {
        repository.loadQuestion(it)
    }

    val repos: LiveData<PagedList<Question>> = Transformations.switchMap(repoResult) { it -> it.data }
    val networkErrors: LiveData<String> = Transformations.switchMap(repoResult) { it -> it.networkErrors }

    fun searchRepo(request: StackRequest): Boolean {
        if (queryLiveData.value != request) {
            queryLiveData.value = request
            return true
        }
        return false
    }

    fun refresh() {
        repository.refresh(queryLiveData.value!!)
    }

    /**
     * Get the last query value.
     */
    fun lastQueryValue(): StackRequest? = queryLiveData.value
}