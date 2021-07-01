package com.softwarica.wheelchairapp.ui.main.Auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softwarica.wheelchairapp.network.model.User
import com.softwarica.wheelchairapp.network.repository.UserRepository
import kotlinx.coroutines.launch

class LoginViewModel() : ViewModel() {
    val repository = UserRepository()
    private val _user = MutableLiveData<User>()
    val user: LiveData<User>
        get() = _user

    fun checkUser(email: String, password: String) {
        viewModelScope.launch {
            _user.value = repository.checkUser(email, password)
        }
    }
}
