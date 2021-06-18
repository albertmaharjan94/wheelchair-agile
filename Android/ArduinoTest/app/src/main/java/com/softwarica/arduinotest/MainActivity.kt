package com.softwarica.arduinotest

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.hardware.usb.UsbManager
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.slider.Slider
import com.google.android.material.switchmaterial.SwitchMaterial
import com.physicaloid.lib.Physicaloid
import com.physicaloid.lib.usb.driver.uart.ReadLisener
import java.nio.charset.StandardCharsets


class MainActivity : AppCompatActivity() {

    private lateinit var mPhysicaloid: Physicaloid
    private lateinit var txtLog: TextView
    private lateinit var txtLog2: TextView
    private lateinit var swKey: SwitchMaterial
    private lateinit var swRev: SwitchMaterial
    private lateinit var swBrake: SwitchMaterial
    private lateinit var sldSpeed: Slider

    private var TAG = "Dashboard"
    private var _key = 0
    private var _brake = 0
    private var _rev = 0
    private var _speed = 0


    private lateinit var arduinoThread: Thread

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mPhysicaloid = Physicaloid(this)
        mPhysicaloid.setBaudrate(9600)

        binding()
        event()
        broadcastUsb()
        dockMicroController()

//      Debug
//        CoroutineScope(Dispatchers.IO).launch {
//            while(true){
//                Log.d(TAG,"$_key#$_brake#$_rev#$_speed")
//            }
//        }

    }

    private fun broadcastUsb() {
        val detachReceiver: BroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (intent.action == UsbManager.ACTION_USB_DEVICE_DETACHED) {
                    unDockMicroController()
                    setText(txtLog2, "Not connected.. retrying")
                } else if (intent.action == UsbManager.ACTION_USB_ACCESSORY_ATTACHED) {
                    dockMicroController()
                    setText(txtLog2, "Connected")
                }
            }
        }

        val filter = IntentFilter()
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED)
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED)
        filter.addAction("android.hardware.usb.action.USB_STATE")
        registerReceiver(detachReceiver, filter)
    }


    private fun binding() {
        txtLog = findViewById(R.id.txtLog)
        txtLog2 = findViewById(R.id.txtLog2)
        swKey = findViewById(R.id.swKey)
        swBrake = findViewById(R.id.swBrake)
        swRev = findViewById(R.id.swRev)
        sldSpeed = findViewById(R.id.sldSpeed)
    }

    private fun event() {
        swKey.setOnCheckedChangeListener { buttonView, isChecked ->
            _key = if (isChecked) 1 else 0
            Toast.makeText(this, "Key", Toast.LENGTH_SHORT).show()
            writeToMicroController()
        }
        swBrake.setOnCheckedChangeListener { buttonView, isChecked ->
            _brake = if (isChecked) 1 else 0
            writeToMicroController()
        }
        swRev.setOnCheckedChangeListener { buttonView, isChecked ->
            _rev = if (isChecked) 1 else 0
            writeToMicroController()
        }
        sldSpeed.addOnChangeListener(Slider.OnChangeListener { slider, value, fromUser ->
            _speed = value.toInt()
            writeToMicroController()
        })
    }

    private fun setText(text: TextView, value: String) {
        runOnUiThread { text.text = value }
    }

    private fun writeToMicroController() {
        if (mPhysicaloid.isOpened) {
            var send = "$_key#$_brake#$_rev#$_speed"
            val buf: ByteArray = send.toByteArray()
            mPhysicaloid.write(buf, buf.size)
        }
    }

    private fun dockMicroController() {
        if (mPhysicaloid.open()) {
            var data = "";
            Log.d(TAG, "Connected")
            var full = ""
            var completed = false
            // receive data
            mPhysicaloid.addReadListener(ReadLisener { size ->
                val buf = ByteArray(1)
                mPhysicaloid.read(buf)
                var data = String(buf, StandardCharsets.UTF_8)
                if(data != "\n") full += data else completed = true

                if(completed){
                    setText(txtLog, full)
                    full = ""
                    completed = false
                }
            })
        } else {
            Toast.makeText(this, "Muji connect bhayena la", Toast.LENGTH_SHORT).show()
            setText(txtLog2, "Not connected.. retrying")
            Log.d(TAG, "Not connected.. retrying")
        }
    }

    override fun onPause() {
        super.onPause()
        unDockMicroController()
    }

    override fun onResume() {
        super.onResume()
        dockMicroController()
    }
    override fun onDestroy() {
        super.onDestroy()
        unDockMicroController()
    }

    private fun unDockMicroController() {
        if (mPhysicaloid.isOpened){
            mPhysicaloid.close()
        }
    }

}