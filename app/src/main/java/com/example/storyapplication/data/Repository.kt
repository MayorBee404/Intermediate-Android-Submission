package com.example.storyapplication.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.paging.*
import com.example.storyapplication.data.datastore.UserPreference
import com.example.storyapplication.data.network.ApiInterceptor
import com.example.storyapplication.data.network.ApiService
import com.example.storyapplication.data.network.UserResponse
import com.example.storyapplication.data.paging.StoryRemoteMediator
import com.example.storyapplication.database.UserStoryDatabase
import com.example.storyapplication.model.StoryModel
import com.example.storyapplication.utilities.AppExecutors
import com.example.storyapplication.view.dashboard.googlemaps.MapType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Repository(
    private val pref: UserPreference,
    private val apiService: ApiService,
    private val userStoryDatabase: UserStoryDatabase,
    val appExecutors: AppExecutors
) {


    fun getUserToken() : LiveData<String> = pref.getUserToken().asLiveData()
    suspend fun saveUserToken(value: String) = pref.saveUserToken(value)

    fun getUserName() : LiveData<String> = pref.getUserName().asLiveData()
    suspend fun saveUserName(value: String) = pref.saveUserName(value)

    fun getUserEmail() : LiveData<String> = pref.getUserEmail().asLiveData()
    suspend fun saveUserEmail(value: String) = pref.saveUserEmail(value)

    fun getIsFirstTime() : LiveData<Boolean> = pref.isFirstTime().asLiveData()
    suspend fun saveIsFirstTime(value: Boolean) = pref.saveIsFirstTime(value)

    fun getMapType() : LiveData<MapType> = pref.getMapType().asLiveData()
    suspend fun saveMapType(value: MapType) = pref.saveMapType(value)

    suspend fun clearCache() = pref.clearCache()

    private fun userStories(token: String): ApiService {
        val client = OkHttpClient.Builder()
            .addInterceptor(ApiInterceptor(token))
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://story-api.dicoding.dev/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(ApiService::class.java)
    }

    fun getUserStoryMapView(token: String) : Call<UserResponse> {
        return userStories(token).getUserStories(1)
    }


    fun userLogin(email: String, password: String) : Call<UserResponse>  {
        val user: Map<String, String> = mapOf(
            "email" to email,
            "password" to password
        )

        return apiService.userLogin(user)
    }

    fun userRegister(name: String, email: String, password: String) : Call<UserResponse>  {
        val user: Map<String, String> = mapOf(
            "name" to name,
            "email" to email,
            "password" to password
        )

        return apiService.userRegister(user)
    }

    @OptIn(ExperimentalPagingApi::class)
    fun getUserStoriesList(token: String) : LiveData <PagingData<StoryModel>>{
        Log.e("getUserStoryList","run List")
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            remoteMediator = StoryRemoteMediator(
                userStoryDatabase, apiService = userStories(token)

            ),
            pagingSourceFactory = {userStoryDatabase.userStoryDao().getAllUserStories()}
        ).liveData

    }

    fun uploadStory(
        photo: MultipartBody.Part,
        description: RequestBody,
        token: String,
        lat: Float? = null,
        lon: Float? = null): Call<UserResponse> = userStories(token).postUserStory(photo, description, lat, lon)


   companion object {
        @Volatile
        private var instance: Repository? = null

        @JvmStatic
        fun getInstance(
            pref: UserPreference,
            apiService: ApiService,
            userStoryDatabase: UserStoryDatabase,
            appExecutors: AppExecutors,
        ) : Repository =
            instance ?: synchronized(this) {
                instance ?: Repository(pref,apiService,userStoryDatabase,appExecutors)
            }.also { instance = it }
    }

}