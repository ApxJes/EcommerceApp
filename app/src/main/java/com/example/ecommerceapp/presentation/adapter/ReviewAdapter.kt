package com.example.ecommerceapp.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerceapp.R
import com.example.ecommerceapp.domain.model.Review

class ReviewAdapter : RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {

    private val differCallback = object : DiffUtil.ItemCallback<Review>() {
        override fun areItemsTheSame(oldItem: Review, newItem: Review): Boolean {
            return oldItem.reviewerEmail == newItem.reviewerEmail && oldItem.date == newItem.date
        }

        override fun areContentsTheSame(oldItem: Review, newItem: Review): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_reviews, parent, false)
        return ReviewViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val review = differ.currentList[position]

        holder.reviewerName.text = review.reviewerName
        holder.reviewerEmail.text = "(${review.reviewerEmail})"
        holder.reviewerComment.text = review.comment
        holder.reviewDate.text = review.date
    }

    override fun getItemCount(): Int = differ.currentList.size

    inner class ReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val reviewerName: TextView = itemView.findViewById(R.id.txvReviewerName)
        val reviewerEmail: TextView = itemView.findViewById(R.id.txvReviewerEmail)
        val reviewerComment: TextView = itemView.findViewById(R.id.txvReviewerComment)
        val reviewDate: TextView = itemView.findViewById(R.id.txvReviewDate)
    }
}

