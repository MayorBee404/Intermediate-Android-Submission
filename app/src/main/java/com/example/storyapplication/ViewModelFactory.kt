package com.example.storyapplication
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.storyapplication.data.Repository
import com.example.storyapplication.data.network.ApiConfig
import com.example.storyapplication.view.authentication.AuthenticationViewModel
import com.example.storyapplication.view.dashboard.home.HomeViewModel
import com.example.storyapplication.view.dashboard.newstory.NewStoryViewModel
import com.example.storyapplication.view.dashboard.setting.SettingViewModel
import java.lang.IllegalArgumentException

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")


class ViewModelFactory private constructor(private val userRepository: Repository) : ViewModelProvider.NewInstanceFactory(){
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when{
            modelClass.isAssignableFrom(AuthenticationViewModel::class.java) -> AuthenticationViewModel(userRepository) as T
            modelClass.isAssignableFrom(SettingViewModel::class.java)->SettingViewModel(userRepository) as T
            modelClass.isAssignableFrom(HomeViewModel::class.java)->HomeViewModel(userRepository) as T
            modelClass.isAssignableFrom(NewStoryViewModel::class.java)->NewStoryViewModel(userRepository) as T
            else -> throw IllegalArgumentException("Unknown ViewModel Class : ${modelClass.name}")

        }
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(
                    ApiConfig.provideUserRepository(context)
                )
            }.also { instance = it }
    }
}