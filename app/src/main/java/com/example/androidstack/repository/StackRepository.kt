package com.example.androidstack.repository

import android.util.Log
import androidx.paging.LivePagedListBuilder
import com.example.androidstack.api.StackService
import com.example.androidstack.api.search
import com.example.androidstack.db.StackCache
import com.example.androidstack.db.StackDao
import com.example.androidstack.model.StackRequest
import com.example.androidstack.model.StackResponse

class StackRepository(
    private val service: StackService,
    private val cache: StackCache
) {

    fun refresh(request: StackRequest) {
        search(service, request, 1, 20, { repos ->

            cache.refresh(repos, request) {
                //lastRequestedPage++
                //isRequestInProgress = false
            }
        }, { error ->
            //_networkErrors.postValue(error)
            //isRequestInProgress = false
        })
    }

    fun loadQuestion(request: StackRequest): StackResponse {
        Log.d("TAGG", "repository: loadQuestion")
        // Get data source factory from the local cache
        val dataSourceFactory = cache.stacksByName(request)

        // every new query creates a new BoundaryCallback
        // The BoundaryCallback will observe when the user reaches to the edges of
        // the list and update the database with extra data
        val boundaryCallback = StackBoundaryCallback(request, service, cache)
        val networkErrors = boundaryCallback.networkErrors

        // Get the paged list
        val data = LivePagedListBuilder(dataSourceFactory, DATABASE_PAGE_SIZE)
            .setBoundaryCallback(boundaryCallback)
            .build()

        // Get the network errors exposed by the boundary callback
        return StackResponse(data, networkErrors)
    }
    

    companion object {
        private const val DATABASE_PAGE_SIZE = 20
    }
}