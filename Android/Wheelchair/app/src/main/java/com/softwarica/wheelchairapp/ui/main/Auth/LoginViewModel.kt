package com.softwarica.wheelchairapp.ui.main.Auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softwarica.wheelchairapp.network.dao.AuthDao
import com.softwarica.wheelchairapp.network.model.User
import com.softwarica.wheelchairapp.network.repository.UserRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val authDao: AuthDao) : ViewModel() {
    val userRepository = UserRepository(authDao)
    private val _user = MutableLiveData<User>()
    val user: LiveData<User>
        get() = _user

    fun checkUser(email: String, password: String) {
         viewModelScope.launch {
            _user.value = userRepository.checkUser(email, password)
        }
    }
}
