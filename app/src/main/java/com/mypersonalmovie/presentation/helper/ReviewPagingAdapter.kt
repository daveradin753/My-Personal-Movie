package com.mypersonalmovie.presentation.helper

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mypersonalmovie.databinding.ItemReviewBinding
import com.mypersonalmovie.domain.model.ReviewModel

class ReviewPagingAdapter : PagingDataAdapter<ReviewModel, ReviewPagingAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val review = getItem(position)
        if (review != null) {
            holder.bind(review)
        }
    }

    class ViewHolder(private val binding: ItemReviewBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(review: ReviewModel) {
            binding.apply {
                tvAuthor.text = review.author
                tvContent.text = review.content
                tvDate.text = review.updatedAt?: review.createdAt
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ReviewModel>() {
            override fun areItemsTheSame(oldItem: ReviewModel, newItem: ReviewModel): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ReviewModel, newItem: ReviewModel): Boolean {
                return oldItem == newItem
            }
        }
    }
}