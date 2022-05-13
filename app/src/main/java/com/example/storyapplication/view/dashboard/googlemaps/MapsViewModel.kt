package com.example.storyapplication.view.dashboard.googlemaps

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapplication.data.Repository
import com.example.storyapplication.data.network.UserResponse
import com.example.storyapplication.model.StoryModel
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapsViewModel(private val  userRepository: Repository) : ViewModel() {
    var myLocationPermission = MutableLiveData<Boolean>()

    fun setMyLocationPermission(value: Boolean) {
        myLocationPermission.value = value
    }

    fun getMapType() : LiveData<MapType> = userRepository.getMapType()

    fun saveMapType(mapType: MapType) {
        viewModelScope.launch {
            userRepository.saveMapType(mapType)
        }
    }


    private var _userStories = MutableLiveData<ArrayList<StoryModel>>()
    val userStories: LiveData<ArrayList<StoryModel>> = _userStories

    private var _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    fun getUserToken() : LiveData<String> = userRepository.getUserToken()

    fun getUserStoriesWithLocation(token: String) {
        val client = userRepository.getUserStoryMapView(token)
        client.enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
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