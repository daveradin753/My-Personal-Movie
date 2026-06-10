package com.mypersonalmovie.presentation.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.mypersonalmovie.R
import com.mypersonalmovie.databinding.ActivityMainBinding
import com.mypersonalmovie.presentation.helper.MoviePagingAdapter
import com.mypersonalmovie.presentation.viewModel.MainViewModel
import com.mypersonalmovie.utils.showErrorDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private val popularAdapter by lazy { MoviePagingAdapter() }
    private val topRatedAdapter by lazy { MoviePagingAdapter(false) }
    private val nowPlayingAdapter by lazy { MoviePagingAdapter(false) }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Notification permission granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Notification permission denied", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setUpObserver()
        setUpUi()
        checkNotificationPermission()
    }

    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun setUpUi() {
        binding.apply {
            toolbar.apply {
                title = getString(R.string.app_name)
                inflateMenu(R.menu.main_menu)
                setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.action_favorite -> {
                            //Move into favorite screen
                            Toast.makeText(this@MainActivity, "Coming Soon", Toast.LENGTH_SHORT).show()
                            true
                        }
                        else -> false
                    }
                }
            }
            setUpPopularMovie()
            setUpTopRatedMovie()
            setUpNowPlayingMovie()
            getPopularMovie()
            getTopRatedMovie()
            getNowPlayingMovie()
        }
    }

    private fun setUpNowPlayingMovie() {
        binding.apply {
            rvNowPlaying.apply {
                adapter = nowPlayingAdapter
                layoutManager =
                    LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            }
            nowPlayingAdapter.addLoadStateListener { loadState ->
                pbNowPlayingMovie.isVisible = loadState.source.refresh is LoadState.Loading

                val isListEmpty = loadState.source.refresh is LoadState.NotLoading && nowPlayingAdapter.itemCount == 0
                if (isListEmpty) {
                    tvEmptyNowPlayingMovie.visibility = View.VISIBLE
                } else {
                    tvEmptyNowPlayingMovie.visibility = View.GONE
                }

                val errorState = loadState.source.refresh as? LoadState.Error
                if (errorState != null) {
                    showErrorDialog(this@MainActivity, errorState.error.message.toString())
                }
            }
            nowPlayingAdapter.setOnItemClickListener {
                DetailMovieActivity.instance(this@MainActivity, it)
            }
        }
    }

    private fun setUpTopRatedMovie() {
        binding.apply {
            rvTopRatedMovie.apply {
                adapter = topRatedAdapter
                layoutManager =
                    LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            }
            topRatedAdapter.addLoadStateListener { loadState ->
                pbTopRatedMovie.isVisible = loadState.source.refresh is LoadState.Loading

                val isListEmpty = loadState.source.refresh is LoadState.NotLoading && topRatedAdapter.itemCount == 0
                if (isListEmpty) {
                    tvEmptyTopRatedMovie.visibility = View.VISIBLE
                } else {
                    tvEmptyTopRatedMovie.visibility = View.GONE
                }

                val errorState = loadState.source.refresh as? LoadState.Error
                if (errorState != null) {
                    showErrorDialog(this@MainActivity, errorState.error.message.toString())
                }
            }
            topRatedAdapter.setOnItemClickListener {
                DetailMovieActivity.instance(this@MainActivity, it)
            }
        }
    }

    private fun setUpPopularMovie() {
        binding.apply {
            rvPopularMovie.apply {
                adapter = popularAdapter
                layoutManager =
                    LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            }
            popularAdapter.addLoadStateListener { loadState ->
                pbPopularMovie.isVisible = loadState.source.refresh is LoadState.Loading

                val isListEmpty = loadState.source.refresh is LoadState.NotLoading && popularAdapter.itemCount == 0
                if (isListEmpty) {
                    tvEmptyPopularMovie.visibility = View.VISIBLE
                } else {
                    tvEmptyPopularMovie.visibility = View.GONE
                }

                val errorState = loadState.source.refresh as? LoadState.Error
                if (errorState != null) {
                    showErrorDialog(this@MainActivity, errorState.error.message.toString())
                }
            }
            popularAdapter.setOnItemClickListener {
                DetailMovieActivity.instance(this@MainActivity, it)
            }
        }
    }

    private fun getNowPlayingMovie() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getNowPlayingMovies().collectLatest {
                    nowPlayingAdapter.submitData(it)
                }
            }
        }
    }

    private fun getTopRatedMovie() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getTopRatedMovies().collectLatest {
                    topRatedAdapter.submitData(it)
                }
            }
        }
    }

    private fun getPopularMovie() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getPopularMovies().collectLatest {
                    popularAdapter.submitData(it)
                }
            }
        }
    }

    private fun setUpObserver() {

    }
}