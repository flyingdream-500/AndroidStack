package com.example.androidstack.ui.recyclerview

import android.util.Log
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.androidstack.R
import com.example.androidstack.model.NetworkState
import com.example.androidstack.model.Question
import com.example.androidstack.ui.recyclerview.viewholders.NetworkStateViewHolder
import com.example.androidstack.ui.recyclerview.viewholders.StackViewHolder
import java.lang.IllegalArgumentException

class StackAdapter(private val retryCallback: () -> Unit) : PagedListAdapter<Question, RecyclerView.ViewHolder>(REPO_COMPARATOR) {

    private var networkState: NetworkState? = null

    private val DATA_VIEW_TYPE = 1
    private val FOOTER_VIEW_TYPE = 2

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when(getItemViewType(position)) {
            DATA_VIEW_TYPE -> (holder as StackViewHolder).bind(getItem(position))
            FOOTER_VIEW_TYPE -> (holder as NetworkStateViewHolder).bind(networkState)
        }
        Log.d("RECYCLERTAG", "super: ${super.getItemCount()} \t itemCount: $itemCount")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            DATA_VIEW_TYPE -> StackViewHolder.create(parent)
            FOOTER_VIEW_TYPE -> NetworkStateViewHolder.create(parent,retryCallback)
            else ->throw IllegalArgumentException("unknown view type $viewType")
        }
    }

    private fun hasExtraRow() = networkState != null && networkState != NetworkState.LOADED

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            FOOTER_VIEW_TYPE
        } else {
            DATA_VIEW_TYPE
        }
    }

    fun setNetworkState(newNetworkState: NetworkState) {
        val previousState = this.networkState
        val hadExtraRow = hasExtraRow()
        this.networkState = newNetworkState
        val hasExtraRow = hasExtraRow()
        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount())
            } else {
                notifyItemInserted(super.getItemCount())
            }
        } else if (hasExtraRow && previousState != newNetworkState) {
                notifyItemChanged(itemCount - 1)
        }

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
