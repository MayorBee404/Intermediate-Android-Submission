package com.example.storyapplication.view.dashboard.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.storyapplication.data.Repository
import com.example.storyapplication.data.network.UserResponse
import com.example.storyapplication.model.StoryModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeViewModel(private val userRepository : Repository) : ViewModel() {

    lateinit var userStories: LiveData<PagingData<StoryModel>>

    private var _message = MutableLiveData<String>()
    val message: LiveData<String> = _message


    fun getUserToken() = userRepository.getUserToken()

    fun getName(): LiveData<String> {
        return userRepository.getUserName()
    }

    fun getUserStories(token: String) {
        userStories = userRepository.getUserStoriesList(token).cachedIn(viewModelScope)
    }
}