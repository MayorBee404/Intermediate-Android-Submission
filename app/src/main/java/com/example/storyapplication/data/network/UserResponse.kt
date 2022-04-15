package com.example.storyapplication.data.network

import com.example.storyapplication.model.StoryModel
import com.example.storyapplication.model.UserModel
import com.google.gson.annotations.SerializedName


data class UserResponse(

    @field:SerializedName("message")
	val message: String? = null,

    @field:SerializedName("loginResult")
	val loginResult: UserModel? = null,

    @field:SerializedName("listStory")
	val listStory: ArrayList<StoryModel>? = null,

    )
