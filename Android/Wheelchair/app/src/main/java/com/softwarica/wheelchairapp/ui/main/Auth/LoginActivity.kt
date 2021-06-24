package com.softwarica.wheelchairapp.ui.main.Auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.softwarica.wheelchairapp.R
import com.softwarica.wheelchairapp.Utils.Validator

class LoginActivity : AppCompatActivity() {

    private lateinit var usernametxt : EditText
    private lateinit var loginbtn : Button
    private lateinit var passwordtxt : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        viewInit()
    }

    private fun viewInit(){
        usernametxt = findViewById(R.id.usernametxt)
        passwordtxt = findViewById(R.id.passwordtxt)
        loginbtn = findViewById(R.id.lgnbtn)

        loginbtn.setOnClickListener({

            var username = usernametxt.text.toString()
            var password = passwordtxt.text.toString()

            if(validate(username, password)){
                login(username, password)
            }
        })
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

    private fun login(username : String, password : String){

    }



}