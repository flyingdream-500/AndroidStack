package com.example.androidstack.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.example.androidstack.api.StackService
import com.example.androidstack.api.search
import com.example.androidstack.db.StackCache
import com.example.androidstack.model.NetworkState
import com.example.androidstack.model.Question
import com.example.androidstack.model.StackRequest
import io.reactivex.rxjava3.functions.Action

class StackBoundaryCallback(
    private val request: StackRequest,
    private val service: StackService,
    private val cache: StackCache
) : PagedList.BoundaryCallback<Question>() {

    companion object {
        private const val NETWORK_PAGE_SIZE = 20
    }

    // keep the last requested page. When the request is successful, increment the page number.
    private var lastRequestedPage = 1

    private val _networkErrors = MutableLiveData<NetworkState>()
    // LiveData of network state.
    val networkErrors: LiveData<NetworkState>
        get() = _networkErrors

    // avoid triggering multiple requests in the same time
    private var isRequestInProgress = false

    var retryFunc: () -> Unit = {requestAndSaveData(request)}


    override fun onItemAtEndLoaded(itemAtEnd: Question) {
        super.onItemAtEndLoaded(itemAtEnd)
        Log.d("TAGG", "onItemAtEndLoaded")
        requestAndSaveData(request)
    }

    override fun onZeroItemsLoaded() {
        super.onZeroItemsLoaded()
        Log.d("TAGG", "onZeroItemsLoaded")
        requestAndSaveData(request)
    }

    private fun requestAndSaveData(request: StackRequest) {
        Log.d("TAGG", "requestAndSaveData  $lastRequestedPage")
        _networkErrors.postValue(NetworkState.LOADING)
        if (isRequestInProgress) return

        isRequestInProgress = true
        search(service, request, lastRequestedPage, NETWORK_PAGE_SIZE, { repos ->
            cache.insert(repos, request) {
                _networkErrors.postValue(NetworkState.LOADED)
                lastRequestedPage++
                isRequestInProgress = false
            }
        }, { error ->
            Log.d("TAGG", "retry change")
            _networkErrors.postValue(NetworkState.error(error))
            isRequestInProgress = false
        })
    }

}