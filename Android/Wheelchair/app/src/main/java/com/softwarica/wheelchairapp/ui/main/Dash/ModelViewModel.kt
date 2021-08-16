package com.softwarica.wheelchairapp.ui.main.Dash

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class ModelViewModel: ViewModel() {
    private var stringSerialData: MutableLiveData<List<String>>? = MutableLiveData(listOf("0", "0", "0", "0"))
    fun init() {
        stringSerialData = MutableLiveData(listOf("0", "0", "0", "0"))
    }

    fun sendSerialData(msg: List<String>) {
        stringSerialData!!.postValue(msg)
    }

    fun getSerialData(): LiveData<List<String>>? {
        return stringSerialData
    }

    private  var reverse: MutableLiveData<Boolean> = MutableLiveData(false);
    fun setReverse(rev: Boolean){
        reverse.postValue(rev)
    }
    fun getReverse(): MutableLiveData<Boolean> {
        return reverse
    }

}