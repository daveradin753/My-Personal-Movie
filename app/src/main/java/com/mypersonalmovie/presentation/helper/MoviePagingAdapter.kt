package com.mypersonalmovie.presentation.helper

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mypersonalmovie.databinding.ItemGeneralMovieBinding
import com.mypersonalmovie.databinding.ItemPopularMovieBinding
import com.mypersonalmovie.domain.model.MovieModel
import com.mypersonalmovie.utils.getBackDropUrl
import com.mypersonalmovie.utils.getPosterUrl

class MoviePagingAdapter(private val isPopularLayout: Boolean = true) :
    PagingDataAdapter<MovieModel, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    class PopularViewHolder(private val binding: ItemPopularMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MovieModel?) {
            binding.apply {
                Glide.with(itemView.context)
                    .load(item?.backdropPath?.getBackDropUrl())
                    .into(ivMoviePoster)
                tvMoviePoster.text = item?.title
            }
        }
    }

    class GeneralViewHolder(private val binding: ItemGeneralMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MovieModel?) {
            binding.apply {
                Glide.with(itemView.context)
                    .load(item?.posterPath?.getPosterUrl())
                    .into(ivMoviePoster)
                tvMovieTitle.text = item?.title
                tvReleaseDate.text = item?.releaseDate
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (isPopularLayout) VIEW_TYPE_POPULAR else VIEW_TYPE_GENERAL
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_POPULAR) {
            PopularViewHolder(
                ItemPopularMovieBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        } else {
            GeneralViewHolder(
                ItemGeneralMovieBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when (holder) {
            is PopularViewHolder -> holder.bind(item)
            is GeneralViewHolder -> holder.bind(item)
        }
    }

    companion object {
        private const val VIEW_TYPE_POPULAR = 1
        private const val VIEW_TYPE_GENERAL = 2

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<MovieModel>() {
            override fun areItemsTheSame(oldItem: MovieModel, newItem: MovieModel): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: MovieModel, newItem: MovieModel): Boolean {
                return oldItem == newItem
            }
        }
    }
}
