package com.example.chinchopaapp

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.activity.viewModels
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chinchopaapp.databinding.ActivityMainBinding
import com.squareup.moshi.Moshi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    val viewModel: MainViewModel by viewModels()

    private val viewBinding by viewBinding(ActivityMainBinding::bind)

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val userAdapter = setupRecyclerView()
        val userRecyclerView = findViewById<View>(R.id.recyclerView).also {
            it.isVisible = false
        }
        val progressBar = findViewById<View>(R.id.progressBar).also {
            it.isVisible = true
        }
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewState.collect {
                    when(it) {
                        is MainViewModel.ViewState.Data -> {
                            userAdapter.userList = it.users
                            userAdapter.notifyDataSetChanged()
                            progressBar.isVisible = false
                            userRecyclerView.isVisible = true
                        }
                        is MainViewModel.ViewState.Loading -> {
                            userAdapter.userList = listOf()
                            progressBar.isVisible = true
                            userRecyclerView.isVisible = false
                        }
                    }
                }
            }
        }
    }

    private fun setupRecyclerView(): UserAdapter {
        val usersRecyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val userAdapter = UserAdapter()
        usersRecyclerView.adapter = userAdapter
        return userAdapter
    }
}