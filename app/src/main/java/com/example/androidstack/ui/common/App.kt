package com.example.androidstack.ui.common

import android.app.Application
import android.content.Context
import com.example.androidstack.di.ContextModule
import com.example.androidstack.di.DaggerStackComponent
import com.example.androidstack.di.StackComponent

class App: Application() {

    lateinit var stackComponent: StackComponent

    override fun onCreate() {
        super.onCreate()
        initDagger()
    }

    private fun initDagger() {
        stackComponent = DaggerStackComponent
            .builder()
            .contextModule(ContextModule(this))
            .build()
    }
}

/**
 * Расширение [Context] для получения [StackComponent]
 */
val Context.stack: StackComponent
get() = when(this) {
    is App -> stackComponent
    else -> this.applicationContext.stack
}

