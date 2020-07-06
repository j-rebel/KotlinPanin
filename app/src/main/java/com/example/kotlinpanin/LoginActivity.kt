package com.example.kotlinpanin

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import io.ktor.client.request.forms.submitForm
import io.ktor.http.Parameters
import io.ktor.util.KtorExperimentalAPI
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.*


class LoginActivity : AppCompatActivity(), CoroutineScope by MainScope() {

    lateinit var pd: ProgressDialog
    val APP_PREFERENCES = "mysettings"
    val APP_PREFERENCES_TOKEN = "TOKEN"
    lateinit var mSettings: SharedPreferences


    @KtorExperimentalAPI
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
        pd = ProgressDialog(this)
        checkBox.setOnClickListener{
            if(checkBox.isChecked) {
                btnRegestrate.isVisible = false
                displayNameInput.isVisible = false
                avatarInput.isVisible = false
                btnLogin.isVisible = true
            } else {
                btnRegestrate.isVisible = true
                displayNameInput.isVisible = true
                avatarInput.isVisible = true
                btnLogin.isVisible = false
            }
        }

        btnLogin.setOnClickListener {
            login(emailInput.text.toString(), passInput.text.toString())
        }
        btnRegestrate.setOnClickListener {
            registrate(emailInput.text.toString(), passInput.text.toString(), displayNameInput.text.toString(), avatarInput.text.toString())
        }
    }

    @KtorExperimentalAPI
    fun login(email: String, password: String) = launch {
        pd.setTitle("Login")
        pd.setMessage("Processing")
        pd.show()
        try {
            val requestedToken = withContext(Dispatchers.IO) {
                val params = Parameters.build {
                    append("email", email)
                    append("password", password)
                }
                Api().client.submitForm<Token>(Api().loginUrl, params, false)// параметры в form
            }
            //Toast.makeText(applicationContext, requestedToken.token, Toast.LENGTH_LONG).show()
            val editor = mSettings.edit();
            editor.putString(APP_PREFERENCES_TOKEN, requestedToken.token)
            editor.apply()
            delay(3000)
            //Toast.makeText(applicationContext, "User authorized", Toast.LENGTH_LONG).show()
            pd.hide()
            val intent = Intent(App.applicationContext(), MainActivity::class.java)
            startActivity(intent)
        } catch (e: Exception) {
            Log.e("Login", e.message, Throwable())
        }
    }

    @KtorExperimentalAPI
    fun registrate(email: String, password: String, displayName: String, avatar: String) = launch {
        try {
            val requestedToken = withContext(Dispatchers.IO) {
                val params = Parameters.build {
                    append("displayName", displayName)
                    append("email", email)
                    append("password", password)
                    append("avatar", avatar)
                }
                Api().client.submitForm<Token>(Api().registrationUrl, params, false)// параметры в form

            }
            //Toast.makeText(applicationContext, requestedToken.token, Toast.LENGTH_LONG).show()
            val editor = mSettings.edit();
            editor.putString(APP_PREFERENCES_TOKEN, requestedToken.token)
            editor.apply()
            Toast.makeText(applicationContext, "User created", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Log.e("Registration", e.message, Throwable())
            Toast.makeText(applicationContext, "Failed to registrate", Toast.LENGTH_LONG).show()
        }
    }
}
