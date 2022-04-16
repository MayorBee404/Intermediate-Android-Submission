package com.example.storyapplication.view.authentication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.storyapplication.model.UserModel
import com.example.storyapplication.utilities.Event

class AuthenticationViewModel : ViewModel() {
    private var _user = MutableLiveData<Event<UserModel>>()
    val user: LiveData<Event<UserModel>> = _user

    private var _loading = MutableLiveData<Event<Boolean>>()
    val loading: LiveData<Event<Boolean>> = _loading

    private var _message = MutableLiveData<Event<String>>()
    val message: LiveData<Event<String>> = _message

    private var _error = MutableLiveData<Event<Boolean>>()
    val error: LiveData<Event<Boolean>> = _error

}