package com.softwarica.arduinotest

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class HandlerTest : AppCompatActivity() {
    var handler: Handler? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_handler_test)
        handler = Handler(Looper.getMainLooper())
        handler!!.postDelayed(object : Runnable {
            override fun run() {
                //call function
                Log.d("Run", "Run here")
                handler!!.postDelayed(this, 10000)
            }
        }, 10000)


    }
}