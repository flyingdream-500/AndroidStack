package com.example.androidstack

import android.app.Application
import androidx.room.Room
import com.example.androidstack.db.StackDatabase

class App: Application() {

    companion object {
        private lateinit var db: StackDatabase

        fun getDataBase(): StackDatabase {
            return db
        }
    }

    override fun onCreate() {
        super.onCreate()
        db = Room.databaseBuilder(applicationContext, StackDatabase::class.java, "questions-db")
            .allowMainThreadQueries()
            .build()
    }
}