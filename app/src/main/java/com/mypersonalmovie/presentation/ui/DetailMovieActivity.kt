package com.mypersonalmovie.presentation.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.mypersonalmovie.R
import com.mypersonalmovie.databinding.ActivityDetailMovieBinding
import com.mypersonalmovie.domain.model.MovieModel
import com.mypersonalmovie.presentation.helper.ReviewPagingAdapter
import com.mypersonalmovie.presentation.viewModel.DetailViewModel
import com.mypersonalmovie.utils.getBackDropUrl
import com.mypersonalmovie.utils.showErrorDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailMovieActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailMovieBinding
    private val viewModel by viewModels<DetailViewModel>()
    private val adapter by lazy { ReviewPagingAdapter() }

    companion object {
        fun instance(context: Context, movieModel: MovieModel?) {
            val intent = Intent(context, DetailMovieActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.putExtra("movieModel", movieModel)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, 0)
            binding.toolbar.setPadding(0, systemBars.top, 0, 0)
            insets
        }
        val movie = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("movieModel", MovieModel::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("movieModel")
        }
        setUpObserver()
        setUpUi(movie)
        getReviews(movie?.id)
        movie?.id?.let { viewModel.isFavoriteMovie(it) }
    }

    private fun getReviews(movieId: Int?) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getMovieReviews(movieId!!).collectLatest {
                    adapter.submitData(it)
                }
            }
        }
    }

    private fun setUpUi(movie: MovieModel?) {
        movie?.let {
            binding.apply {
                toolbar.apply {
                    title = "Detail Movie"
                    setNavigationOnClickListener { finish() }
                    inflateMenu(R.menu.detail_movie_menu)
                    setOnMenuItemClickListener { menuItem ->
                        when (menuItem.itemId) {
                            R.id.action_share -> {
                                showShareBottomSheet(it.title)
                                true
                            }
                            R.id.action_favorite -> {
                                val isFavorite = viewModel.isFavorite.value
                                if (isFavorite) {
                                    viewModel.deleteFavoriteMovie(movie.id!!)
                                } else {
                                    viewModel.insertFavoriteMovie(movie)
                                }
                                true
                            }
                            else -> false
                        }
                    }
                }
                tvMovieTitle.text = it.title
                tvMovieDescription.text = it.overview

                Glide.with(this@DetailMovieActivity)
                    .load(it.backdropPath?.getBackDropUrl())
                    .placeholder(R.drawable.gradient_shadow)
                    .into(ivMovieImage)

                rvReviews.apply {
                    adapter = this@DetailMovieActivity.adapter
                    layoutManager = LinearLayoutManager(this@DetailMovieActivity, LinearLayoutManager.VERTICAL, false)
                }
                adapter.addLoadStateListener { loadState ->
                    pbReview.isVisible = loadState.source.refresh is LoadState.Loading

                    val isListEmpty = loadState.source.refresh is LoadState.NotLoading && adapter.itemCount == 0
                    if (isListEmpty) {
                        tvEmptyReview.visibility = View.VISIBLE
                    } else {
                        tvEmptyReview.visibility = View.GONE
                    }

                    val errorState = loadState.source.refresh as? LoadState.Error
                    if (errorState != null) {
                        showErrorDialog(this@DetailMovieActivity, errorState.error.message.toString())
                    }
                }
            }
        }
    }

    private fun setUpObserver() {
        insertObserver()
        deleteObserver()
        errorObserver()
        loadingObserver()
        favoriteObserver()
    }

    private fun favoriteObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isFavorite.collectLatest { isFavorite ->
                    val favoriteItem = binding.toolbar.menu.findItem(R.id.action_favorite)
                    val color = if (isFavorite) {
                        getColor(R.color.yellow)
                    } else {
                        getColor(R.color.white)
                    }
                    favoriteItem?.icon?.let { icon ->
                        DrawableCompat.setTint(icon, color)
                    }
                }
            }
        }
    }

    private fun loadingObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.loading.collectLatest {
                    binding.pbLoading.isVisible = it
                }
            }
        }
    }

    private fun errorObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.error.collectLatest {
                    if (it != null) {
                        showErrorDialog(this@DetailMovieActivity, it)
                    }
                }
            }
        }
    }

    private fun deleteObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.deleteFavoriteMovie.collectLatest {
                    Log.d("DetailMovieActivity", "deleteObserver: $it")
                }
            }
        }
    }

    private fun insertObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.insertFavoriteMovie.collectLatest {
                    Log.d("DetailMovieActivity", "insertObserver: $it")
                }
            }
        }
    }

    private fun showShareBottomSheet(movieTitle: String?) {
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.layout_share_bottom_sheet, binding.root, false)
        val tvTitle = view.findViewById<TextView>(R.id.tv_share_movie_title)
        val btnCopy = view.findViewById<Button>(R.id.btn_share_copy_title)

        tvTitle?.text = movieTitle

        btnCopy?.setOnClickListener {
            val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Movie Title", movieTitle)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(this, getString(R.string.title_copied), Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }

        dialog.setContentView(view)
        dialog.show()
    }
}