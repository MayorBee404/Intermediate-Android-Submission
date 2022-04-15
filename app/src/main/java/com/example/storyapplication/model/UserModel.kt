package com.example.storyapplication.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserModel(
    @field:SerializedName("name")
    val name: String? = null,
    val email: String? = null,
    @field:SerializedName("userId")
    val userId: String? = null,
    @field:SerializedName("token")
    val token: String? = null

):Parcelable