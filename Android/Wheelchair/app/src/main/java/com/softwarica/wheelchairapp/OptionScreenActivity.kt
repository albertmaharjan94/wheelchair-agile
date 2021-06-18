package com.softwarica.wheelchairapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.softwarica.wheelchairapp.Utils.Constants

class OptionScreenActivity : AppCompatActivity() {

    private lateinit var remotebtn : ImageView
    private lateinit var dockbtn : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_option_screen)

        remotebtn = findViewById(R.id.remotemode)
        dockbtn = findViewById(R.id.dockmode)

        dockbtn.setOnClickListener({
            var intent : Intent = Intent(this, TabActivity::class.java)
            intent.putExtra(Constants.MODE, Constants.DOCK)
            startActivity(intent)
        })

        remotebtn.setOnClickListener({
            var intent : Intent = Intent(this, TabActivity::class.java)
            intent.putExtra(Constants.MODE, Constants.REMOTE)
            startActivity(intent)
        })

    }
}