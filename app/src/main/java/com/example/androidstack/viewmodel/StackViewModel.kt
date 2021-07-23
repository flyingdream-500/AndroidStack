package com.example.androidstack.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.example.androidstack.model.NetworkState
import com.example.androidstack.model.Question
import com.example.androidstack.model.StackRequest
import com.example.androidstack.model.StackResponse
import com.example.androidstack.repository.StackRepository

class StackViewModel(private val repository: StackRepository): ViewModel() {

    private val queryLiveData = MutableLiveData<StackRequest>()
    private val repoResult: LiveData<StackResponse> = Transformations.map(queryLiveData) {
        repository.loadQuestion(it)
    }

    val repos: LiveData<PagedList<Question>> = Transformations.switchMap(repoResult) { it -> it.data }
    val networkStates: LiveData<NetworkState> = Transformations.switchMap(repoResult) { it -> it.networkStates }


    fun searchRepo(request: StackRequest): Boolean {
        if (queryLiveData.value != request) {
            queryLiveData.value = request
            return true
        }
        return false
    }

    fun getRefreshState(): MutableLiveData<NetworkState> {
        return repository.refreshState
    }

    fun refresh() {
        repository.refresh(queryLiveData.value!!)
    }

    fun retry() {
        Log.d("TAGG", "retry()")
        repository.retry()
    }
}