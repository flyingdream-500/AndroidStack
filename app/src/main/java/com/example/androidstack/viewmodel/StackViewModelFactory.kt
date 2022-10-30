package com.example.androidstack.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.androidstack.repository.StackRepository

class StackViewModelFactory(private val stackRepository: StackRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        StackViewModel(stackRepository) as T

}