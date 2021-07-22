package com.example.androidstack.di

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.example.androidstack.App
import com.example.androidstack.api.StackService
import com.example.androidstack.db.StackCache
import com.example.androidstack.db.StackDatabase
import com.example.androidstack.repository.StackRepository
import com.example.androidstack.viewmodel.ViewModelFactory
import java.util.concurrent.Executors

object Injection {

    private fun provideCache(context: Context): StackCache {
        val database = App.getDataBase()
        return StackCache(database.stackDao(), Executors.newSingleThreadExecutor())
    }

    /**
     * Creates an instance of [GithubRepository] based on the [GithubService] and a
     * [GithubLocalCache]
     */
    private fun provideGithubRepository(context: Context): StackRepository {
        return StackRepository(StackService.create(), provideCache(context))
    }

    /**
     * Provides the [ViewModelProvider.Factory] that is then used to get a reference to
     * [ViewModel] objects.
     */
    fun provideViewModelFactory(context: Context): ViewModelProvider.Factory {
        return ViewModelFactory(provideGithubRepository(context))
    }
}