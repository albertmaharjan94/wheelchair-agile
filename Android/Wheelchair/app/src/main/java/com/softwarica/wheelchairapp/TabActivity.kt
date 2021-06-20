package com.softwarica.wheelchairapp

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayout
import com.softwarica.wheelchairapp.Utils.Constants
import com.softwarica.wheelchairapp.ViewPager.CustomViewPager
import com.softwarica.wheelchairapp.ui.main.SectionsPagerAdapter
import io.ghyeok.stickyswitch.widget.StickySwitch

class TabActivity : AppCompatActivity() {
    private lateinit var startbtn : StickySwitch;
    private lateinit var reverse : ImageButton;
    private lateinit var forward : ImageButton;
    private lateinit var headlights : ImageButton;
    private lateinit var headlights_off : ImageButton;
    private lateinit var connectLay : RelativeLayout;
    private  lateinit var utilityLay : LinearLayout;
    private lateinit var conntxt : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tab)

        val mode = intent.getStringExtra(Constants.MODE)


        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager, mode!!)
        val viewPager: CustomViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter

        val tabs: TabLayout = findViewById(R.id.tabs)

        tabs.setupWithViewPager(viewPager)

        utilityLay = findViewById(R.id.utilityLay)

        forward = findViewById(R.id.forward)
        reverse = findViewById(R.id.reverse)
        headlights = findViewById(R.id.headlights)
        headlights_off = findViewById(R.id.headlights_off)

        connectLay = findViewById(R.id.connectLay);

        startbtn = findViewById(R.id.startbtn)
        startbtn.setOnClickListener {
            Log.d("TAG", "onCreate: " + startbtn.getDirection())
            if(startbtn.getDirection().toString().equals("RIGHT")){
                wheelChairStart()
            }else if(startbtn.getDirection().toString().equals("LEFT")){
                wheelChairStop()
            }

        }

        if(mode == Constants.REMOTE){
            connectLay.visibility = View.GONE
        }else if(mode == Constants.DOCK){
            checkConnection()
        }

        reverse.setOnClickListener({
                changeState("REV")
        })
        forward.setOnClickListener({
            changeState("FWD")
        })
        headlights.setOnClickListener({
            changeState("HDON")
        })
        headlights_off.setOnClickListener({
            changeState("HOFF")
        })
    }

    fun checkConnection(){

    }



<<<<<<< HEAD
<<<<<<< HEAD
    fun changeState(mode : String){
=======
=======
>>>>>>> 136efdd (dpad with serial)
    fun changeState(mode: String){
>>>>>>> 136efdd (dpad with serial)
        when(mode){
            "FWD" -> {
                forward.visibility = View.GONE; reverse.visibility = View.VISIBLE;
            }
            "REV" -> {
                forward.visibility = View.VISIBLE; reverse.visibility = View.GONE;
            }
            "HDON" -> {
                headlights_off.visibility = View.VISIBLE ; headlights.visibility = View.GONE;
            }
            "HOFF"-> {
                headlights_off.visibility = View.GONE ; headlights.visibility = View.VISIBLE;
            }
            else->{
                forward.visibility = View.GONE; reverse.visibility = View.VISIBLE;
            }


        }
    }

    fun wheelChairStart () {
        utilityLay.visibility = View.VISIBLE
    }

    fun wheelChairStop(){
        utilityLay.visibility = View.GONE
        forward.visibility = View.GONE
        headlights_off.visibility = View.GONE

        reverse.visibility = View.VISIBLE
        headlights.visibility = View.VISIBLE
    }
<<<<<<< HEAD
=======

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

    companion object{

         const val CONNECTING_STATUS =
            1 // used in bluetooth handler to identify message status

         const val MESSAGE_READ = 2 // used in bluetooth handler to identify message update

        var handler: Handler? = null;

        var connectedThread : ConnectedThread ?= null

        /* =============================== Thread for Data Transfer =========================================== */
        class ConnectedThread(private val mmSocket: BluetoothSocket) : Thread() {
            private val mmInStream: InputStream?
            private val mmOutStream: OutputStream?
            override fun run() {
//                val buffer = ByteArray(1024) // buffer store for the stream
//                var bytes = 0 // bytes returned from read()
//                // Keep listening to the InputStream until an exception occurs
//                while (true) {
//                    try {
//                        /*
//                        Read from the InputStream from Arduino until termination character is reached.
//                        Then send the whole String message to GUI Handler.
//                         */
//                        buffer[bytes] = mmInStream?.read() as Byte
//                        var readMessage: String
//                        if (buffer[bytes].equals("\n")) {
//                            readMessage = String(buffer, 0, bytes)
//                            Log.e("Arduino Message", readMessage)
//                            handler?.obtainMessage(MESSAGE_READ, readMessage)?.sendToTarget()
//                            bytes = 0
//                        } else {
//                            bytes++
//                        }
//                    } catch (e: IOException) {
//                        e.printStackTrace()
//                        break
//                    }
//                }
            }

            /* Call this from the main activity to send data to the remote device */
            fun write(input: String) {
                try {
                    val bytes = input.toByteArray() //converts entered String into bytes
                    mmOutStream?.write(bytes)
                } catch (e: IOException) {
                    Log.e("Send Error", "Unable to send message", e)
                }
            }

            /* Call this from the main activity to shutdown the connection */
            fun cancel() {
                try {
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
                }
                mmInStream = tmpIn
                mmOutStream = tmpOut
            }
        }
    }
>>>>>>> 136efdd (dpad with serial)
}