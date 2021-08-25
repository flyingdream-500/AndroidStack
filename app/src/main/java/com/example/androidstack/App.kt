package com.example.androidstack

import android.app.Application
import android.content.Context
import com.example.androidstack.di.ContextModule
import com.example.androidstack.di.DaggerStackComponent
import com.example.androidstack.di.StackComponent

class App: Application() {

    lateinit var stackComponent: StackComponent

    override fun onCreate() {
        super.onCreate()
        stackComponent = DaggerStackComponent
            .builder()
            .contextModule(ContextModule(this))
            .build()
    }
}

val Context.stack: StackComponent
get() = when(this) {
    is App -> stackComponent
    else -> this.applicationContext.stack
}

