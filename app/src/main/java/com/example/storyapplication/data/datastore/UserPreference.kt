package com.example.storyapplication.data.datastore

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreference private constructor(private val dataStore: DataStore<Preferences>) {

    private val FIRST_TIME_KEY = booleanPreferencesKey("first_time")
    private val USER_TOKEN_KEY = stringPreferencesKey("user_token")
    private val USER_EMAIL_KEY = stringPreferencesKey("user_email")
    private val USER_NAME_KEY = stringPreferencesKey("user_name")



    fun getUserToken(): Flow<String> = dataStore.data.map {
        it[USER_TOKEN_KEY] ?: DEFAULT_VALUE
    }

    suspend fun saveUserToken(token: String) {
        dataStore.edit {
            it[USER_TOKEN_KEY] = token
            Log.e("User Preference", "Token saved! saveUserToken: $token")
        }
    }

    fun getUserEmail(): Flow<String> = dataStore.data.map {
        it[USER_EMAIL_KEY] ?: DEFAULT_VALUE
    }

    suspend fun saveUserEmail(email: String) {
        dataStore.edit {
            it[USER_EMAIL_KEY] = email
        }
    }

    fun getUserName(): Flow<String> = dataStore.data.map {
        it[USER_NAME_KEY] ?: DEFAULT_VALUE
    }

    suspend fun saveUserName(name: String) {
        dataStore.edit {
            it[USER_NAME_KEY] = name
        }
    }

    fun isFirstTime(): Flow<Boolean> = dataStore.data.map {
        it[FIRST_TIME_KEY] ?: true
    }

    suspend fun saveIsFirstTime(firstTime: Boolean) {
        dataStore.edit {
            it[FIRST_TIME_KEY] = firstTime
        }
    }

    suspend fun clearCache() {
        dataStore.edit {
            it.clear()
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null
        const val DEFAULT_VALUE = "not_set_yet"

        fun getInstance(dataStore: DataStore<Preferences>) : UserPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}
