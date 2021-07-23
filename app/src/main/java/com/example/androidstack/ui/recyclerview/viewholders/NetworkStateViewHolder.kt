package com.example.androidstack.ui.recyclerview.viewholders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.androidstack.R
import com.example.androidstack.model.NetworkState
import com.example.androidstack.model.Status

class NetworkStateViewHolder(view: View,
                             private val retryCallback: () -> Unit) : RecyclerView.ViewHolder(view) {

    private val progressBar = view.findViewById<ProgressBar>(R.id.pb_footer)
    private val retry = view.findViewById<TextView>(R.id.tv_footer_retry)
    private val errorMessage = view.findViewById<TextView>(R.id.tv_footer_error)

    private fun toVisibility(b: Boolean): Int {
        return if (b) View.VISIBLE else View.GONE
    }

     init {
         retry.setOnClickListener {
             retryCallback()
         }
     }

    fun bind(networkState: NetworkState?) {
        progressBar.visibility = toVisibility(networkState?.status == Status.RUNNING)
        retry.visibility = toVisibility(networkState?.status == Status.FAILED)
        errorMessage.visibility = toVisibility(networkState?.msg != null)
        errorMessage.text = networkState?.msg
    }

    companion object {
        fun create(parent: ViewGroup, retryCallback: () -> Unit): NetworkStateViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list_footer, parent, false)
            return NetworkStateViewHolder(view, retryCallback)
        }
    }
}