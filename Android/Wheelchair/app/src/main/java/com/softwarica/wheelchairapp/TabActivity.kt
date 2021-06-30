package com.softwarica.wheelchairapp

import android.app.ProgressDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.*
import android.os.*
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.softwarica.wheelchairapp.Utils.Constants
import com.softwarica.wheelchairapp.ViewPager.CustomViewPager
import com.softwarica.wheelchairapp.adapters.DeviceListAdapter
import com.softwarica.wheelchairapp.services.UsbService
import com.softwarica.wheelchairapp.ui.main.Dash.ModelViewModel
import com.softwarica.wheelchairapp.ui.main.SectionsPagerAdapter
import io.ghyeok.stickyswitch.widget.StickySwitch
import java.io.*


class TabActivity : AppCompatActivity() {
    private lateinit var startbtn: StickySwitch
    private lateinit var reverse: ImageButton
    private lateinit var forward: ImageButton
    private lateinit var headlights: ImageButton
    private lateinit var headlights_off: ImageButton
    private lateinit var connectLay: RelativeLayout
    private lateinit var utilityLay: LinearLayout
    private lateinit var conntxt: TextView
    private lateinit var txtDebugger: TextView
    private var bt_status = false
    private var modelViewModel: ModelViewModel? = null

    var mode: String? = null
    var progress: ProgressDialog? = null
    private var usbService: UsbService? = null
    private val uHandler = UsbHandler(this)
    private val bHandler = BluetoothHandler(this)


    //   arduino values
    var _key = 0
    var _brake = 1
    var _reverse = 0
    var _speed_1 = 0

    private val usbConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(arg0: ComponentName, arg1: IBinder) {
            usbService = (arg1 as UsbService.UsbBinder).service
            usbService!!.setHandler(uHandler)
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            usbService = null
        }
    }

    private val mUsbReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            // default no usb connection
            when (intent.action) {
                UsbService.ACTION_USB_PERMISSION_GRANTED -> {
                    USB_STATE = 1
                    conntxt.text = "USB Connected"
                    connectLay.visibility = View.GONE
//                "USB Permission connected"
                }
                UsbService.ACTION_USB_PERMISSION_NOT_GRANTED -> {
                    conntxt.text = "USB Permission not granted"
                    USB_STATE = 2
                    connectLay.visibility = View.VISIBLE
                    wheelChairStop()

//                   "USB Permission not granted"
                }
                UsbService.ACTION_NO_USB -> {
                    USB_STATE = 3
                    conntxt.text = "No USB connected"
                    wheelChairStop()
                    connectLay.visibility = View.VISIBLE
//                    "No USB connected"
                }
                UsbService.ACTION_USB_DISCONNECTED -> {
                    conntxt.text = "USB disconnected"
                    wheelChairStop()
                    USB_STATE = 4
                    connectLay.visibility = View.VISIBLE

//                    "USB disconnected"
                }
                UsbService.ACTION_USB_NOT_SUPPORTED -> {
                    wheelChairStop()
                    USB_STATE = 5
                    connectLay.visibility = View.VISIBLE
                }

//                   "USB device not supported"
            }

            connectLay.visibility = View.GONE
        }
    }

    override fun onResume() {
        super.onResume()
        mode = intent.getStringExtra(Constants.MODE)!!
        bt_status = intent.getBooleanExtra(Constants.BT_STATUS, false)
        if (mode == Constants.DOCK) {
            setFilters() // Start listening notifications from UsbService


            startService(
                UsbService::class.java,
                usbConnection,
                null
            )
        } else {
            setBtFilters()
        }
    }

    override fun onPause() {
        super.onPause()
        if (mode == Constants.DOCK) {
            unregisterReceiver(mUsbReceiver)
            unbindService(usbConnection)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(mReceiver)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tab)
        mode = intent.getStringExtra(Constants.MODE)
        bt_status = intent.getBooleanExtra(Constants.BT_STATUS, false)
        progress = ProgressDialog(this@TabActivity)
        progress?.setCancelable(false)
        viewInit()


        if (mode == Constants.REMOTE) {
            remoteConn()
        } else if (mode == Constants.DOCK) {
            usbConn()
        }

    }

    private fun usbConn() {
        connectLay.visibility = View.GONE
        startService(
            UsbService::class.java,
            usbConnection,
            null
        )
    }

    var mReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            val action = intent.action
            Log.e("BTACTION", action.toString())
            if (action == BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED) {
                conntxt.text = "Disconnecting"
                progress!!.setTitle("Bluetooth Disconnecting");
                progress!!.setMessage("Bluetooth device is disconnecting")
                progress!!.show()
            }
            if (action == BluetoothDevice.ACTION_ACL_DISCONNECTED) {
                progress?.dismiss()
                conntxt.text = "Disconnected"
                bt_status = false
                wheelChairStop()
                startbtn.setDirection(StickySwitch.Direction.LEFT)
                viewReconBT()
            }
            if (action == BluetoothDevice.ACTION_ACL_CONNECTED) {
                progress?.dismiss()
                conntxt.text = "Connected"
                bt_status = true
            }
        }
    }
    var alert_pending = false
    fun viewReconBT() {
        if(!alert_pending){
            alert_pending = true
            AlertDialog.Builder(this@TabActivity)
                .setTitle("Bluetooth disconnected")
                .setCancelable(false)
                .setMessage("Do you want to connect again?")
                .setPositiveButton("Yes") { dialog, id ->
//                        start connection thread again
                    try {

                        progress!!.setTitle("Bluetooth Connection");
                        progress!!.setMessage("Please wait while we connect to devices...")
                        progress!!.show()

                        closeThreads()
                        BluetoothFragment.Companion.CreateConnectionThread(
                            this,
                            BluetoothFragment.current_address,
                            false
                        ).start()
                        SystemClock.sleep(500)

                        connectedThread = ConnectedThread(BluetoothFragment.mmSocket!!, this, bHandler)
                        connectedThread?.start()
                    } catch (e: Exception) {
                        Toast.makeText(
                            this@TabActivity,
                            "Connection Error. Please try again",
                            Toast.LENGTH_SHORT
                        ).show()
                        e.printStackTrace()
                    }
                    dialog.dismiss()
                    alert_pending = false
                }
                .setNegativeButton("No") { dialog, id ->
                    dialog.dismiss()
                    alert_pending = false
                }
                .show()
        }

    }

    private fun closeThreads() {
        connectedThread?.cancel()
        connectedThread = null
        BluetoothFragment.mmSocket?.close()
        BluetoothFragment.mmSocket = null
    }

    fun viewInit() {
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager, mode!!)
        val viewPager: CustomViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter

        modelViewModel = ViewModelProvider(this).get(ModelViewModel::class.java)
        modelViewModel!!.init()

        val tabs: TabLayout = findViewById(R.id.tabs)

        tabs.setupWithViewPager(viewPager)

        utilityLay = findViewById(R.id.utilityLay)

        forward = findViewById(R.id.forward)
        reverse = findViewById(R.id.reverse)
        headlights = findViewById(R.id.headlights)
        headlights_off = findViewById(R.id.headlights_off)
        conntxt = findViewById(R.id.connTxt)
        txtDebugger = findViewById(R.id.txtDebugger)
        connectLay = findViewById(R.id.connectLay)

//        if(!Constants.DEBUG){
//            txtDebugger.visibility = View.GONE
//        }
        startbtn = findViewById(R.id.startbtn)
        startbtn.setOnClickListener {
            Log.d("TAG", "onCreate: " + startbtn.getDirection())
            if(!bt_status && mode == Constants.REMOTE){
                startbtn.setDirection(StickySwitch.Direction.LEFT)
                bHandler.obtainMessage(101).sendToTarget()
            }else{
                if (startbtn.getDirection().toString() == "RIGHT") {
                    wheelChairStart()
                } else if (startbtn.getDirection().toString() == "LEFT") {
                    wheelChairStop()
                }
            }


        }
        txtDebugger.text = mode

        reverse.setOnClickListener {
            changeState("REV")
        }
        forward.setOnClickListener {
            changeState("FWD")
        }
        headlights.setOnClickListener {
            changeState("HDON")
        }
        headlights_off.setOnClickListener {
            changeState("HOFF")
        }

    }

    private fun setBtFilters() {
        val filter = IntentFilter()
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED)
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED)
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED)
        registerReceiver(mReceiver, filter)
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

    fun remoteConn() {
//        handler = object : Handler(Looper.getMainLooper()) {
//            override fun handleMessage(msg: Message) {
//
//                Log.d("TAG", "handleMessage: " + msg.what)
//                when (msg.what) {
//                    CONNECTING_STATUS -> {
//                        when (msg.arg1) {
//                            1 -> {
//                                conntxt.text = "Connected"
//
//                            }
//                            -1 -> {
//                                conntxt.text = "Not Connected"
//                            }
//                            else -> {
//                                conntxt.text = "Network Error"
//                            }
//                        }
//                    }
//                    MESSAGE_READ -> {
//                        val arduinoMsg: String = msg.obj.toString() // Read message from Arduino
//                        when (arduinoMsg.toLowerCase()) {
//                            "led is turned on" -> {
//                            }
//                            "led is turned off" -> {
//
//                            }
//                        }
//                    }
//                }
//            }
//        }

        connectLay.visibility = View.GONE
        BluetoothFragment.mHandler = object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message) {
                when (msg.what) {
//                    BluetoothFragment.LOADING_DIALOG -> {
////                        progress.
//                        if (progress != null) {
//                            progress!!.setTitle("Bluetooth Connection");
//                            progress!!.setMessage("Please wait while we connect to devices...");
//                            progress!!.show()
//                        }
//                    }
                    BluetoothFragment.CLOSE_LOADING_DIALOG -> {
                        if (progress != null) {
                            progress!!.dismiss()
                        }
                        closeThreads()
                        bt_status = false
                        AlertDialog.Builder(this@TabActivity)
                            .setTitle("Connection Error")
                            .setMessage("Make sure bluetooth devices are connected")
                            .setPositiveButton("Okay") { dialog, id ->
                                dialog.dismiss()
                            }
                            .show()

                    }

                }
            }
        }
        if (bt_status) {
//            handler?.obtainMessage(CONNECTING_STATUS, 1, -1)?.sendToTarget();
            conntxt.text = "Connected"
            connectedThread = ConnectedThread(BluetoothFragment.mmSocket!!, this, bHandler)
            connectedThread?.start()
        }


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

    fun changeState(mode: String) {
        when (mode) {
            "FWD" -> {
                forward.visibility = View.GONE
                reverse.visibility = View.VISIBLE
                _reverse = 0
                writeToArduino()
            }
            "REV" -> {
                forward.visibility = View.VISIBLE; reverse.visibility = View.GONE;
                _reverse = 1
                writeToArduino()
            }
            "HDON" -> {
                headlights_off.visibility = View.VISIBLE; headlights.visibility = View.GONE;
            }
            "HOFF" -> {
                headlights_off.visibility = View.GONE; headlights.visibility = View.VISIBLE;
            }
            else -> {
                forward.visibility = View.GONE; reverse.visibility = View.VISIBLE;
            }
        }
    }

    private fun writeToArduino() {
        try {
            val out = "$_key#$_brake#$_reverse#$_speed_1\n"
            if (mode == Constants.DOCK) {
                usbService?.write(out.toByteArray())
            }
            if (mode == Constants.REMOTE) {
                connectedThread?.write(out)
            }
        } catch (e: Exception) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun wheelChairStart() {
        _key = 1
        _brake = 0
        writeToArduino()
        utilityLay.visibility = View.VISIBLE
    }

    fun wheelChairStop() {
        _key = 0
        _reverse = 0
        _brake = 1
        writeToArduino()
        utilityLay.visibility = View.GONE
        forward.visibility = View.GONE
        headlights_off.visibility = View.GONE

        reverse.visibility = View.VISIBLE
        headlights.visibility = View.VISIBLE
    }

    override fun onBackPressed() {
        // Terminate Bluetooth Connection and close app
        if (connectedThread != null) {
            connectedThread?.cancel()
        }
        val a = Intent(Intent.ACTION_MAIN)
        a.addCategory(Intent.CATEGORY_HOME)
        a.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(a)
    }


    private class UsbHandler(val activity: TabActivity) : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                UsbService.MESSAGE_FROM_SERIAL_PORT -> {
                    try {
                        activity.runOnUiThread {
                            val data = msg.obj.toString()
                        }
                    } catch (e: Exception) {
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
                    val stats =
                        "${buffer[1].trim()} ${buffer[2].trim()} ${buffer[3].trim()} ${buffer[4].trim()}"
                    activity.txtDebugger.text = stats
                    activity.modelViewModel!!.sendSerialData(buffer)
                }
            }
        }

    }

    inner class BluetoothHandler(val activity: TabActivity) : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            Log.e("btHandler", msg.toString())
            when (msg.what) {
                100 -> {
                    try {
                        activity.runOnUiThread {
                            val data = msg.obj.toString()
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                101 -> {
                    if (!bt_status) {
                        viewReconBT()
                    }
                }
                102 -> Toast.makeText(
                    activity,
                    "DSR_CHANGE",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

    }


    companion object {

        const val CONNECTING_STATUS = 1 // used in bluetooth handler to identify message status

        var USB_STATE = 3

        const val MESSAGE_READ = 2 // used in bluetooth handler to identify message update

        var handler: Handler? = null;

        var connectedThread: ConnectedThread? = null

        /* =============================== Thread for Data Transfer =========================================== */
        class ConnectedThread(
            private val mmSocket: BluetoothSocket,
            private val activity: TabActivity,
            private val bHandler: BluetoothHandler
        ) : Thread() {
            private var mmInStream: InputStream?
            private var mmOutStream: OutputStream?
            private var input: BufferedReader? = null
            var running = true
            override fun run() {
                ByteArray(1024) // buffer store for the stream
                // Keep listening to the InputStream until an exception occursZ
                while (running) {
                    try {
                        val buffer = ByteArray(128)
                        var readMessage: String
                        var bytes: Int
                        if (mmInStream!!.available() > 2) {
                            try {
                                // Read from the InputStream
                                bytes = mmInStream!!.read(buffer)
                                readMessage = String(buffer, 0, bytes)
                                val split = readMessage.split("#")
                                if (split.isNotEmpty()) {
                                    val commaSplit = split[split.size - 1].split(",")

                                    if (commaSplit.size >= 6) {
                                        Log.d("bt_read", commaSplit.toString())
                                    }
                                }
                            } catch (e: IOException) {
                                Log.e("err", "disconnected", e)
                                break
                            }
                            // Send the obtained bytes to the UI Activity
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
//                    try {
//                        /*
//                        Read from the InputStream from Arduino until termination character is reached.
//                        Then send the whole String message to GUI Handler.
//                         */
//
//                        var readMessage: String = ""
//                        var receiving = true
//                        while(receiving){
//                            var value = mmInStream!!.read().toChar()!!
//                            if(value.toString() == "#"){
//                                receiving = false
//                                break
//                            }else readMessage+=value
//                        }
//                        activity.runOnUiThread {
//                            activity.txtDebugger.text = readMessage
//                        }
////                        activity.modelViewModel!!.sendSerialData(readMessage)
//                        Log.d("bluetooth_thread_read", readMessage)
//
//
//                    } catch (e: IOException) {
//                        e.printStackTrace()
//                        break
//                    }
                }
            }

            /* Call this from the main activity to send data to the remote device */
            fun write(input: String) {
                try {
                    val bytes = input.toByteArray() //converts entered String into bytes
                    mmOutStream?.write(bytes)
                    Log.d("bluetooth_thread_write", bytes.toString())
                } catch (e: IOException) {
                    bHandler.obtainMessage(101).sendToTarget()
                    Log.e("Send Error", "Unable to send message", e)
                }
            }

            /* Call this from the main activity to shutdown the connection */
            fun cancel() {
                try {
                    running = false
                    mmInStream = null
                    mmOutStream = null
                    mmSocket.close()

                } catch (e: IOException) {
                }
            }

            init {
                var tmpIn: InputStream? = null
                var tmpOut: OutputStream? = null

                // Get the input and output streams, using temp objects because
                // member streams are final
                try {
                    tmpIn = mmSocket.inputStream
                    tmpOut = mmSocket.outputStream
                } catch (e: IOException) {
                    Log.e("StreamErr", "Socket stream")
                }
                mmInStream = tmpIn
                mmOutStream = tmpOut
            }
        }
    }
}