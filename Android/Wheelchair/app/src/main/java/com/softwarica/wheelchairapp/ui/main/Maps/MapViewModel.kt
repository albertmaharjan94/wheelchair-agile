package com.softwarica.wheelchairapp.ui.main.Maps

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MapViewModel : ViewModel() {
    private var speed: MutableLiveData<CharSequence> = MutableLiveData()

    fun setSpeed(input: CharSequence){
        speed.postValue(input)
        Log.d("Speed Mapviewmodel", speed.value.toString())
    }

    fun getSpeed(): LiveData<CharSequence>{
        return speed
    }

}