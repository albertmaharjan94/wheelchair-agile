package com.softwarica.wheelchairapp.ui.main.Auth

import android.bluetooth.BluetoothHearingAid
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.softwarica.wheelchairapp.OptionScreenActivity
import com.softwarica.wheelchairapp.R
import com.softwarica.wheelchairapp.Utils.Validator
import com.softwarica.wheelchairapp.network.dao.AuthDao
import com.softwarica.wheelchairapp.network.database_conf.WheelDB
import com.softwarica.wheelchairapp.network.model.User
import com.softwarica.wheelchairapp.network.repository.UserRepository
//import com.softwarica.wheelchairapp.ui.main.Activity.TrackActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

import kotlinx.coroutines.Dispatchers.Main

class LoginActivity : AppCompatActivity() {

    private lateinit var usernametxt : EditText
    private lateinit var loginbtn : Button
    private lateinit var passwordtxt : EditText
    private lateinit var viewModel: LoginViewModel
    private lateinit var authInstance : AuthDao
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        viewInit()
    }

    private fun viewInit(){
        usernametxt = findViewById(R.id.usernametxt)
        passwordtxt = findViewById(R.id.passwordtxt)
        loginbtn = findViewById(R.id.lgnbtn)

        authInstance = WheelDB.getinstance(this).getAuthDao()
        viewModel =
            ViewModelProvider(this, LoginViewModelFactory(authInstance)).get(LoginViewModel::class.java)

        loginbtn.setOnClickListener {
            var username = usernametxt.text.toString()
            var password = passwordtxt.text.toString()

            if (validate(username, password)) {
                login(username, password)
            }
        }


    }


    private fun validate(username : String, password : String)  : Boolean{
        var err = 0
        if(!Validator.validateFields(username)){
            err ++;
            usernametxt.setError("Username must not be empty!")
        }
        if(Validator.validatePassord(password) == 1){
            err ++;
            passwordtxt.setError("Password length must be greater than 6!")
        }
        if(Validator.validatePassord(password) == 2){
            err++;
            passwordtxt.setError("Password must not be empty!")
        }
        if(err == 0){
            return true
        }
                return false;
    }

    private fun login(email : String, password : String){
        viewModel.checkUser(email,password)
        var data : User? = null

        viewModel.user.observe(this, {
            data = it
            Log.d("LoginData",data.toString())
            if(data != null){
                saveUser(email, password)
                CoroutineScope(Dispatchers.IO).launch {
                    UserRepository(authInstance).getProfile(password)
                    withContext(Main){
                        startActivity(
                            Intent(this@LoginActivity, OptionScreenActivity::class.java)
                        )
                    }
                }
            }
            else{
                Toast.makeText(this, "Email or password is incorrect", Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun saveUser(email: String , password: String) {
        val sharedPref = getSharedPreferences(
            "userPref",
            MODE_PRIVATE
        )
        val editor = sharedPref?.edit()
        editor?.putString("email", email)
        editor?.putString("password", password)
        editor?.apply()
    }



}