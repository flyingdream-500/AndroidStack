package com.example.androidstack.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.androidstack.repository.StackRepository

class ViewModelFactory(private val repository: StackRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StackViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StackViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}