package com.softwarica.wheelchairapp

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothHeadset
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.softwarica.wheelchairapp.Utils.Constants
import com.softwarica.wheelchairapp.network.api.ServiceBuilder
import com.softwarica.wheelchairapp.ui.main.Auth.LoginActivity


class OptionScreenActivity : AppCompatActivity() {

    private lateinit var remotebtn : ImageView
    private lateinit var dockbtn : ImageView
    private lateinit var linearLayout : LinearLayout
    private lateinit var linearLayoutBt : LinearLayout

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            configLandScape()
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            configPortrait()
        }
    }

    private fun configLandScape() {
        linearLayout?.updateLayoutParams<ConstraintLayout.LayoutParams> {
            this.width =0
            this.height = ConstraintLayout.LayoutParams.MATCH_PARENT
            this.topToTop = linearLayout!!.parent.hashCode()
            this.startToEnd = linearLayout!!.parent.hashCode()
            this.matchConstraintPercentWidth = 0.5f
            this.matchConstraintPercentHeight= 1f
            this.horizontalChainStyle = ConstraintLayout.LayoutParams.CHAIN_PACKED
            this.matchConstraintDefaultWidth = ConstraintLayout.LayoutParams.MATCH_CONSTRAINT_PERCENT
        }
        linearLayoutBt?.updateLayoutParams<ConstraintLayout.LayoutParams> {
            this.width =0
            this.height = ConstraintLayout.LayoutParams.MATCH_PARENT
            this.topToTop = linearLayoutBt!!.parent.hashCode()
            this.startToEnd =linearLayout!!.id
            this.matchConstraintPercentWidth = 0.5f
            this.matchConstraintPercentHeight= 1f
            this.horizontalChainStyle = ConstraintLayout.LayoutParams.CHAIN_PACKED
            this.matchConstraintDefaultWidth = ConstraintLayout.LayoutParams.MATCH_CONSTRAINT_PERCENT
        }
    }

    fun configPortrait(){
        linearLayout?.updateLayoutParams<ConstraintLayout.LayoutParams> {
            this.width = ConstraintLayout.LayoutParams.MATCH_PARENT
            this.height = 0
            this.topToTop = linearLayout!!.parent.hashCode()
            this.startToEnd = linearLayout!!.parent.hashCode()
            this.matchConstraintPercentWidth = 1f
            this.matchConstraintPercentHeight= 0.5f
            this.verticalChainStyle = ConstraintLayout.LayoutParams.CHAIN_PACKED
            this.matchConstraintDefaultHeight= ConstraintLayout.LayoutParams.MATCH_CONSTRAINT_PERCENT
            this.matchConstraintDefaultWidth = ConstraintLayout.LayoutParams.MATCH_CONSTRAINT_PERCENT
        }

        linearLayoutBt?.updateLayoutParams<ConstraintLayout.LayoutParams> {
            this.width = ConstraintLayout.LayoutParams.MATCH_PARENT
            this.height = 0
            this.topToTop = ConstraintLayout.LayoutParams.UNSET
            this.topToBottom = linearLayout!!.id
            this.startToEnd = linearLayoutBt!!.parent.hashCode()
            this.matchConstraintPercentWidth = 1f
            this.verticalChainStyle = ConstraintLayout.LayoutParams.CHAIN_PACKED
            this.matchConstraintPercentHeight= 0.5f
            this.matchConstraintDefaultHeight= ConstraintLayout.LayoutParams.MATCH_CONSTRAINT_PERCENT
            this.matchConstraintDefaultWidth = ConstraintLayout.LayoutParams.MATCH_CONSTRAINT_PERCENT
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_option_screen)

        linearLayout = findViewById(R.id.linearLayout)
        linearLayoutBt = findViewById(R.id.linearLayoutBt)
        remotebtn = findViewById(R.id.remotemode)
        dockbtn = findViewById(R.id.dockmode)
        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // In landscape
            configLandScape()
        } else {
            // In portrait
            configPortrait()
        }
        val userDetail = ServiceBuilder.logged_user

        if(userDetail == null && !Constants.DEBUG_MODE){
            startActivity(Intent(this@OptionScreenActivity, LoginActivity::class.java))
            finish()
        }

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