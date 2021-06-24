package com.softwarica.arduinotest

import android.content.*
import android.os.*
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.slider.Slider
import com.google.android.material.switchmaterial.SwitchMaterial
import com.softwarica.arduinotest.service.UsbService


class MainActivity : AppCompatActivity() {

    private val mUsbReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                UsbService.ACTION_USB_PERMISSION_GRANTED ->
                    txtLog2.text = "USB Ready"
                UsbService.ACTION_USB_PERMISSION_NOT_GRANTED ->
                    txtLog2.text = "USB Permission not granted"
                UsbService.ACTION_NO_USB ->
                    txtLog2.text = "No USB connected"
                UsbService.ACTION_USB_DISCONNECTED ->
                    txtLog2.text = "USB disconnected"
                UsbService.ACTION_USB_NOT_SUPPORTED ->
                    txtLog2.text = "USB device not supported"
            }
        }
    }
    private var usbService: UsbService? = null
    private val mHandler = MyHandler(this)
    private val usbConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(arg0: ComponentName, arg1: IBinder) {
            Toast.makeText(this@MainActivity, "Connected", Toast.LENGTH_SHORT).show()
            usbService = (arg1 as UsbService.UsbBinder).service
            usbService!!.setHandler(mHandler)
        }
        override fun onServiceDisconnected(arg0: ComponentName) {
            Toast.makeText(this@MainActivity, "Disconnected", Toast.LENGTH_SHORT).show()
            usbService = null
        }
    }

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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding()
        event()

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
            writeToSerial()
        }
        swBrake.setOnCheckedChangeListener { buttonView, isChecked ->
            _brake = if (isChecked) 1 else 0
            writeToSerial()
        }
        swRev.setOnCheckedChangeListener { _, isChecked ->
            _rev = if (isChecked) 1 else 0
            writeToSerial()
        }
        sldSpeed.addOnChangeListener(Slider.OnChangeListener { _, value, _ ->
            _speed = value.toInt()
            writeToSerial()
        })
    }

    private fun writeToSerial() {
        if (usbService != null) { // if UsbService was correctly bound, Send data
            var data = "${_key}#${_brake}#${_rev}#${_speed}\n"
            usbService!!.write(data.toByteArray())
        }
    }

    private fun setText(text: TextView, value: String) {
        runOnUiThread { text.text = value }
    }


    override fun onPause() {
        super.onPause()
        unregisterReceiver(mUsbReceiver)
        unbindService(usbConnection)
    }

    override fun onResume() {
        super.onResume()
        setFilters() // Start listening notifications from UsbService

        startService(
            UsbService::class.java,
            usbConnection,
            null
        )

    }
    override fun onDestroy() {
        super.onDestroy()

    }

    private fun startService(
        service: Class<*>,
        serviceConnection: ServiceConnection,
        extras: Bundle?
    ) {
        if (!UsbService.SERVICE_CONNECTED) {
            val startService = Intent(this, service)
            if (extras != null && !extras.isEmpty) {
                val keys = extras.keySet()
                for (key in keys) {
                    val extra = extras.getString(key)
                    startService.putExtra(key, extra)
                }
            }
            startService(startService)
        }
        val bindingIntent = Intent(this, service)
        bindService(bindingIntent, serviceConnection, BIND_AUTO_CREATE)
    }

    private fun setFilters() {
        val filter = IntentFilter()
        filter.addAction(UsbService.ACTION_USB_PERMISSION_GRANTED)
        filter.addAction(UsbService.ACTION_NO_USB)
        filter.addAction(UsbService.ACTION_USB_DISCONNECTED)
        filter.addAction(UsbService.ACTION_USB_NOT_SUPPORTED)
        filter.addAction(UsbService.ACTION_USB_PERMISSION_NOT_GRANTED)
        registerReceiver(mUsbReceiver, filter)
    }

    private class MyHandler(val activity: MainActivity) : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                UsbService.MESSAGE_FROM_SERIAL_PORT -> {
                    try{
                        activity.runOnUiThread{
                            val data = msg.obj.toString()
                            activity.txtLog.text = data
                        }
                    }catch (e:Exception){
                        e.printStackTrace()
                    }

                }
                UsbService.CTS_CHANGE -> Toast.makeText(
                    activity,
                    "CTS_CHANGE",
                    Toast.LENGTH_LONG
                ).show()
                UsbService.DSR_CHANGE -> Toast.makeText(
                    activity,
                    "DSR_CHANGE",
                    Toast.LENGTH_LONG
                ).show()
                UsbService.SYNC_READ -> {
                    val buffer = msg.obj as List<String>
                    val stats = "${buffer[1].trim()} ${buffer[2].trim()} ${buffer[3].trim()} ${buffer[4].trim()}"
                    activity.txtLog.text = stats
                }
            }
        }

    }


}