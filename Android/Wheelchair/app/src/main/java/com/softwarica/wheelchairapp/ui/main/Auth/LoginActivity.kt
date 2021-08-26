package com.softwarica.wheelchairapp.ui.main.Auth

//import com.softwarica.wheelchairapp.ui.main.Activity.TrackActivity

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton
import com.softwarica.wheelchairapp.OptionScreenActivity
import com.softwarica.wheelchairapp.R
import com.softwarica.wheelchairapp.Utils.Constants
import com.softwarica.wheelchairapp.Utils.Validator
import com.softwarica.wheelchairapp.network.api.ServiceBuilder
import com.softwarica.wheelchairapp.network.dao.AuthDao
import com.softwarica.wheelchairapp.network.database_conf.WheelDB
import com.softwarica.wheelchairapp.network.model.User
import com.softwarica.wheelchairapp.network.repository.UserRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main


class LoginActivity : AppCompatActivity() {

    private lateinit var usernametxt : EditText
    private lateinit var loginbtn : CircularProgressButton
    private lateinit var passwordtxt : EditText
    private lateinit var viewModel: LoginViewModel
    private lateinit var authInstance : AuthDao
    private lateinit var btnDebug: ImageButton
    private lateinit var progress: ProgressDialog
    private var count = 0
    private var startMillis: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        viewInit()
    }

    private fun viewInit(){
        usernametxt = findViewById(R.id.usernametxt)
        passwordtxt = findViewById(R.id.passwordtxt)
        loginbtn = findViewById(R.id.lgnbtn)
        btnDebug = findViewById(R.id.btnDebug)
        progress = ProgressDialog(this)



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

        btnDebug.setOnClickListener{
            val time = System.currentTimeMillis()
            if (startMillis == 0L || time - startMillis > 5000) {
                startMillis = time
                count = 1
            } else { //  time-startMillis< 3000
                count++
            }

            if (count == 10) {
                //do whatever you need
                val alertDialog: AlertDialog = this.let {

                    val builder = AlertDialog.Builder(it)

                    val inflater = this.layoutInflater;

                    builder.setView(inflater.inflate(R.layout.pin_debug, null))
                        // Add action buttons
                        .setPositiveButton("Enter"
                        ) { dialog, _ ->
                            val dialogObj: Dialog = Dialog::class.java.cast(dialog)
                            val pin = dialogObj.findViewById<EditText>(R.id.username).text
                            if(pin.toString() == Constants.DEBUG_CODE){
                                Toast.makeText(this, "Debugger Mode ON", Toast.LENGTH_SHORT).show()
                                Constants.DEBUG_MODE = true
                                startActivity(
                                    Intent(this@LoginActivity, OptionScreenActivity::class.java)
                                )
                            }
                        }
                        .setNegativeButton("Cancel"
                        ) { dialog, _ ->
                            dialog.dismiss()
                        }
                    builder.create()

                }
                alertDialog.show()
                alertDialog.setCancelable(false)
                Toast.makeText(this, "Developer Mode", Toast.LENGTH_SHORT).show()
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

        progress.setTitle("Logging")
        progress.setMessage("Login in, please wait")
        progress.setCancelable(false)
        progress.show()
        ServiceBuilder.token = null

        CoroutineScope(Dispatchers.IO).launch{
            authInstance = WheelDB.getinstance(this@LoginActivity).getAuthDao()
            data = UserRepository(authInstance).checkUser(email, password)

            Log.d("LoginData",data.toString())
            if(ServiceBuilder.token =="-1"){
                runOnUiThread{

                    progress.dismiss()
                    Toast.makeText(this@LoginActivity, "Unable to connect server", Toast.LENGTH_SHORT).show()
                }
            }
            else{
                if(data != null){
                    saveUser(email, password)
                    CoroutineScope(Dispatchers.IO).launch {
                        UserRepository(authInstance).getProfile(password)
                        withContext(Main){
                            progress.dismiss()
                            startActivity(
                                Intent(this@LoginActivity, OptionScreenActivity::class.java)
                            )
                        }
                    }
                }
                else{
                    runOnUiThread{
                        progress.dismiss()
                        Toast.makeText(this@LoginActivity, "Email or password is incorrect", Toast.LENGTH_SHORT).show()
                    }

                }
            }

        }

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