package com.example.androidstack.ui.recyclerview.viewholders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.androidstack.R
import com.example.androidstack.model.Question
import com.example.androidstack.util.*
import kotlinx.android.synthetic.main.item_question.view.*

class StackViewHolder(view: View): RecyclerView.ViewHolder(view) {


    fun bind(question: Question?) {
        if (question != null) {
            itemView.tv_title.text = getEncodedTitle(question)
            itemView.tv_viewsCount.text = getViews(question)
            itemView.tv_answersCount.text = question.answerCount.toString()
            itemView.tv_createdDate.text = getDateWith(question.creationDate, "asked")
            itemView.tv_lastActiveDate.text = getDateWith(question.activityDate, "active")

            itemView.tv_vote.text = question.score.toString()
            itemView.tv_vote.setTextColor(colorOfVotes(itemView.context, question.score))

            //show icons only for accepted and closed questions
            getIconState(itemView.context, question).let{ itemView.iv_questionState.setImageDrawable(it) }

            val owner = question.owner
            itemView.tv_userName.text = owner.displayName
            Glide.with(itemView)
                .load(owner.profileImage)
                .placeholder(R.drawable.placeholder_profile_image)
                .error(R.drawable.placeholder_profile_image)
                .into(itemView.iv_userLogo)

            Glide.with(itemView)
                .load(getLogoUrl(question.tags))
                .placeholder(R.drawable.others)
                .error(R.drawable.others)
                .into(itemView.iv_questionLogo)

        }

    }

    companion object {
        fun create(parent: ViewGroup): StackViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_question, parent, false)
            return StackViewHolder(view)
        }
    }
}