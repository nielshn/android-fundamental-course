package com.dicoding.restaurantreview.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.restaurantreview.data.response.CustomerReviewsItem
import com.dicoding.restaurantreview.databinding.ItemReviewBinding

class ReviewAdapter : ListAdapter<CustomerReviewsItem, ReviewAdapter.MyViewHolder>(DIFF_CALLBACK) {
    class MyViewHolder(val binding: ItemReviewBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(review: CustomerReviewsItem) {
            binding.tvItem.text = "${review.review}\n- ${review.name}"
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CustomerReviewsItem>() {
            override fun areItemsTheSame(
                oldItem: CustomerReviewsItem,
                newItem: CustomerReviewsItem
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: CustomerReviewsItem,
                newItem: CustomerReviewsItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewAdapter.MyViewHolder {
        val binding = ItemReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReviewAdapter.MyViewHolder, position: Int) {
        val review = getItem(position)
        holder.bind(review)
    }
}