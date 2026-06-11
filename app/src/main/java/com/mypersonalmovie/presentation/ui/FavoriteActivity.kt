package com.mypersonalmovie.presentation.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.mypersonalmovie.R
import com.mypersonalmovie.databinding.ActivityFavoriteBinding
import com.mypersonalmovie.presentation.helper.FavoriteAdapter
import com.mypersonalmovie.presentation.viewModel.FavoriteViewModel
import com.mypersonalmovie.utils.showErrorDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private val adapter by lazy { FavoriteAdapter() }
    private val viewModel by viewModels<FavoriteViewModel>()

    companion object {
        fun instance(context: Context) {
            val intent = Intent(context, FavoriteActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setUpObserver()
        seetUpUi()
    }

    private fun seetUpUi() {
        binding.apply {
            toolbar.apply {
                title = getString(R.string.favorite_movie)
                setNavigationOnClickListener {
                    onBackPressedDispatcher.onBackPressed()
                }
            }
            rvContent.apply {
                adapter = this@FavoriteActivity.adapter
                layoutManager = LinearLayoutManager(this@FavoriteActivity, LinearLayoutManager.VERTICAL, false)
            }
            adapter.setOnItemClickListener {
                DetailMovieActivity.instance(this@FavoriteActivity, it)
            }
            viewModel.getAlllFavoriteMovie()
        }
    }

    private fun setUpObserver() {
        errorObserver()
        listFavoriteObserver()
    }

    private fun listFavoriteObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.listFavorite.collect {
                    adapter.setItems(it)
                }
            }
        }
    }

    private fun errorObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.error.collect {
                    if (it != null) {
                        showErrorDialog(this@FavoriteActivity, it)
                    }
                }
            }
        }
    }
}