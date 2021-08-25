package com.example.androidstack.db

import android.util.Log
import androidx.paging.DataSource
import com.example.androidstack.model.Question
import com.example.androidstack.model.StackRequest
import com.example.androidstack.util.ORDER_DESC
import com.example.androidstack.util.SORT_ACTIVITY
import com.example.androidstack.util.SORT_CREATION
import com.example.androidstack.util.SORT_VOTES
import java.util.concurrent.Executor

class StackCache(
    private val stackDao: StackDao,
    private val ioExecutor: Executor
    ) {

    fun insert(stacks: List<Question>, request: StackRequest, insertFinished: () -> Unit) {
        ioExecutor.execute {
            stacks.forEach {
                it.query = request.query
                it.sort = request.sort
                it.order = request.order
            }
            stackDao.insert(stacks)
            insertFinished()
        }
    }

    fun refresh(stacks: List<Question>, request: StackRequest, insertFinished: () -> Unit) {
        ioExecutor.execute {
            stackDao.clearCache(request.query, request.sort, request.order)
            stacks.forEach {
                it.query = request.query
                it.sort = request.sort
                it.order = request.order
            }
            stackDao.insert(stacks)
            insertFinished()
        }
    }

    fun stacksByName(request: StackRequest): DataSource.Factory<Int, Question> {
        val name = request.query
        val sort = request.sort
        val order = request.order
        return when (sort) {
            SORT_VOTES -> return if (order == ORDER_DESC) stackDao.stackByVotesDesc(name, sort, order) else stackDao.stackByVotesAsc(name, sort, order)
            SORT_ACTIVITY -> return if (order == ORDER_DESC) stackDao.stackByActivityDesc(name, sort, order) else stackDao.stackByActivityAsc(name, sort, order)
            SORT_CREATION -> return if (order == ORDER_DESC) stackDao.stackByCreationDesc(name, sort, order) else stackDao.stackByCreationAsc(name, sort, order)
            else -> stackDao.stackByCreationAsc(name, sort, order)
        }
    }

    fun clear() {
        ioExecutor.execute {
            stackDao.clearAll()
        }
    }
}