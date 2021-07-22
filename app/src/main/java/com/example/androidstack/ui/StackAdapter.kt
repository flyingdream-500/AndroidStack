package com.example.androidstack.ui

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.androidstack.model.Question

class StackAdapter : PagedListAdapter<Question, RecyclerView.ViewHolder>(REPO_COMPARATOR) {


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val question = getItem(position)
        if (question != null) {
            (holder as StackViewHolder).bind(question)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return StackViewHolder.create(parent)
    }

    companion object {
        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<Question>() {
            override fun areItemsTheSame(oldItem: Question, newItem: Question): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Question, newItem: Question): Boolean =
                oldItem == newItem
        }
    }
}
