package com.example.androidstack.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.androidstack.model.OwnerConverter
import com.example.androidstack.model.Question

@Database(
    entities = [Question::class],
    version = 1)
@TypeConverters(OwnerConverter::class)
abstract class StackDatabase : RoomDatabase() {
    abstract fun stackDao(): StackDao
}