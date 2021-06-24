package com.softwarica.wheelchairapp.ui.main.Dash

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class ModelViewModel: ViewModel() {
    private var stringSerialData: MutableLiveData<List<String>>? = MutableLiveData(listOf("0", "0", "0", "0"))
    var _speed: MutableLiveData<String> = MutableLiveData("")
    fun init() {
        stringSerialData = MutableLiveData(listOf("0", "0", "0", "0"))
    }

    fun sendSerialData(msg: List<String>) {
        stringSerialData!!.postValue(msg)
        _speed.postValue(msg[0])
    }

    fun getSerialData(): LiveData<List<String>>? {
        return stringSerialData
    }

    fun getSpeed (): LiveData<String> {
        return _speed
    }
}