package com.softwarica.wheelchairapp

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothHeadset
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.softwarica.wheelchairapp.Utils.Constants


class OptionScreenActivity : AppCompatActivity() {

    private lateinit var remotebtn : ImageView
    private lateinit var dockbtn : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_option_screen)

        remotebtn = findViewById(R.id.remotemode)
        dockbtn = findViewById(R.id.dockmode)

        val bluetoothDialog = BluetoothFragment()

        dockbtn.setOnClickListener {
            var intent: Intent = Intent(this, TabActivity::class.java)
            intent.putExtra(Constants.MODE, Constants.DOCK)
            startActivity(intent)
        }

        remotebtn.setOnClickListener {
            if (isBluetoothConnected()) {
                var intent = Intent(this, TabActivity::class.java)
                intent.putExtra(Constants.MODE, Constants.REMOTE)
                startActivity(intent)
            } else {
                bluetoothDialog.show(supportFragmentManager, "BluetoothDialogFragment")
            }
        }

    }


    private fun isBluetoothConnected () : Boolean{
        val mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        return (mBluetoothAdapter != null && mBluetoothAdapter.isEnabled
                && mBluetoothAdapter.getProfileConnectionState(BluetoothHeadset.HEADSET) == BluetoothHeadset.STATE_CONNECTED)
    }
}