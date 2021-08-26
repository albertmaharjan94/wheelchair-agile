package com.softwarica.wheelchairapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softwarica.wheelchairapp.network.model.User
import kotlinx.coroutines.launch


class TablViewModel: ViewModel() {
    private var stringSerialData: MutableLiveData<List<String>>? = MutableLiveData(listOf("0", "0", "0", "0"))

    private  var speed: MutableLiveData<Int> = MutableLiveData(0);
    fun setSpeed(s: Int){
        speed.postValue(s)
    }
    fun getSpeed(): MutableLiveData<Int> {
        return speed
    }


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