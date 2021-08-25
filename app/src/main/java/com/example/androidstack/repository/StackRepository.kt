package com.example.androidstack.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.LivePagedListBuilder
import com.example.androidstack.api.StackService
import com.example.androidstack.api.search
import com.example.androidstack.db.StackCache
import com.example.androidstack.model.NetworkState
import com.example.androidstack.model.StackRequest
import com.example.androidstack.model.StackResponse

class StackRepository(
    private val service: StackService,
    private val cache: StackCache
) {

    private val _refreshState = MutableLiveData<NetworkState>()

    val refreshState: LiveData<NetworkState>
    get() = _refreshState


    var retryFunc: () -> Unit = {}

    fun refresh(request: StackRequest) {
        _refreshState.postValue(NetworkState.LOADING)
        search(service, request, 1, DATABASE_PAGE_SIZE, { repos ->

            cache.refresh(repos, request) {
                _refreshState.postValue(NetworkState.LOADED)
            }
        }, { error ->
            _refreshState.postValue(NetworkState.error(error))
        })
    }

    fun loadQuestion(request: StackRequest): StackResponse {
        // Get data source factory from the local cache
        val dataSourceFactory = cache.stacksByName(request)

        // every new query creates a new BoundaryCallback
        // The BoundaryCallback will observe when the user reaches to the edges of
        // the list and update the database with extra data
        val boundaryCallback = StackBoundaryCallback(request, service, cache)
        val networkErrors = boundaryCallback.networkErrors
        retryFunc = boundaryCallback.retryFunc

        // Get the paged list
        val data = LivePagedListBuilder(dataSourceFactory, DATABASE_PAGE_SIZE)
            .setBoundaryCallback(boundaryCallback)
            .build()

        // Get the network errors exposed by the boundary callback
        return StackResponse(data, networkErrors)
    }

    fun retry() {
        retryFunc.invoke()
    }
    

    companion object {
        private const val DATABASE_PAGE_SIZE = 20
    }
}