package com.softwarica.wheelchairapp.ui.main.Auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.softwarica.wheelchairapp.network.dao.AuthDao

class LoginViewModelFactory (val authDao: AuthDao): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return LoginViewModel(authDao) as T
    }

}
