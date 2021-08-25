package com.example.androidstack.di

import com.example.androidstack.MainActivity
import com.example.androidstack.db.StackCache
import com.example.androidstack.repository.StackRepository
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [CacheModule::class, ContentModule::class])
interface StackComponent {
    fun inject(mainActivity: MainActivity)
}