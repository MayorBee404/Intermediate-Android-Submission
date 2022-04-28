package com.example.storyapplication.view.dashboard.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.storyapplication.data.Repository
import com.example.storyapplication.data.network.UserResponse
import com.example.storyapplication.model.StoryModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeViewModel(private val userRepository : Repository) : ViewModel() {

    private var _userStories = MutableLiveData<ArrayList<StoryModel>>()
    val userStories: LiveData<ArrayList<StoryModel>> = _userStories

    private var _message = MutableLiveData<String>()

    fun getUserToken() = userRepository.getUserToken()

    fun getUserStories(token : String){
        val client = userRepository .getUserStories(token)
        client.enqueue(object : Callback<UserResponse> {
            override fun onResponse(call : Call<UserResponse>, response: Response <UserResponse>){
                if(response.isSuccessful){
                    val userResponse = response.body()?.listStory
                    userRepository.appExecutors.networkIO.execute {
                        _userStories.postValue(userResponse!!)

                    }
                }

            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                _message.value = t.message
            }
        })
    }
}