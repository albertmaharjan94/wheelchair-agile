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



    fun changeState(mode : String){
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
}