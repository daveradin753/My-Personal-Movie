package com.mypersonalmovie.presentation.helper

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mypersonalmovie.databinding.ItemFavoriteBinding
import com.mypersonalmovie.domain.model.MovieModel
import com.mypersonalmovie.utils.getPosterUrl

class FavoriteAdapter: RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {

     private var items: List<MovieModel> = emptyList()
     @SuppressLint("NotifyDataSetChanged")
     fun setItems(items: List<MovieModel>) {
         this.items = items
         notifyDataSetChanged()
     }

    private var onItemClick: (MovieModel) -> Unit = {}
    fun setOnItemClickListener(listener: (MovieModel) -> Unit) {
        onItemClick = listener
    }

     override fun onCreateViewHolder(
         parent: ViewGroup,
         viewType: Int,
     ): ViewHolder {
         val binding = ItemFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
         return ViewHolder(binding)
     }

     override fun onBindViewHolder(
         holder: ViewHolder,
         position: Int,
     ) {
         val item = items[position]
         holder.bind(item)
     }

     override fun getItemCount(): Int {
         return items.size
     }

     inner class ViewHolder(private val binding: ItemFavoriteBinding): RecyclerView.ViewHolder(binding.root) {
         fun bind(item: MovieModel) {
             binding.apply {
                 tvMovieTitle.text = item.title
                 tvMovieDescription.text = item.overview
                 tvMovieScore.text = item.voteAverage.toString()
                 tvReleaseDate.text = item.releaseDate
                 Glide.with(itemView.context)
                     .load(item.posterPath?.getPosterUrl())
                     .into(ivMoviePoster)
                 root.setOnClickListener {
                     onItemClick(item)
                 }
             }
         }

     }
}