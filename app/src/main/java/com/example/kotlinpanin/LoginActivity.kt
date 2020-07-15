package com.example.kotlinpanin

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import io.ktor.client.request.forms.submitForm
import io.ktor.http.Parameters
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.*
import androidx.core.content.edit
import androidx.lifecycle.lifecycleScope
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse

class LoginActivity : AppCompatActivity() {

    lateinit var pd: ProgressDialog
    lateinit var mSettings: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("LoginActivity", "onCreate")
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

    override fun onDestroy() {
        super.onDestroy()
        Log.d("LoginActivity", "onDestroy")
        finish()
    }

    override fun onStop() {
        super.onStop()
        Log.d("LoginActivity", "onStop")
        finish()
    }

    override fun onPause() {
        super.onPause()
        Log.d("LoginActivity", "onPause")
        finish()
    }

    fun login(email: String, password: String) = lifecycleScope.launch {
        pd.setCancelable(false)
        pd.setTitle("Login")
        pd.setMessage("Processing")
        pd.show()
        try {
            val params = Parameters.build {
                append("email", email)
                append("password", password)
            }
            val request = Api.client.submitForm<HttpResponse>(Api.loginUrl, params, false).headers["Set-Cookie"].toString()
            val cookie = request.substring(0, request.indexOf(";"))
            val requestedToken = Api.client.submitForm<Token>(Api.loginUrl, params, false)// параметры в form
            Log.i("cookie", cookie)
            mSettings.edit {
                putString(APP_PREFERENCES_TOKEN, requestedToken.token)
                //putString(APP_PREFERENCES_COOKIE, cookie)
            }
            delay(3000)
            pd.hide()
            pd.dismiss()
            val intent = Intent(App.applicationContext(), MainActivity::class.java)
            startActivity(intent)
        } catch (e: Exception) {
            Log.e("Login", e.message, Throwable())
        } finally {
            pd.hide()
            pd.dismiss()
        }
    }

    fun registrate(email: String, password: String, displayName: String, avatar: String) = lifecycleScope.launch {
        pd.setCancelable(false)
        pd.setTitle("New user")
        pd.setMessage("Creating")
        pd.show()
        try {
            val params = Parameters.build {
                append("displayName", displayName)
                append("email", email)
                append("password", password)
                append("avatar", avatar)
            }
            Api.client.submitForm<Token>(Api.registrationUrl, params, false)
            delay(2000)
            login(email, password)
        } catch (e: Exception) {
            Log.e("Registration", e.message, Throwable())
            Toast.makeText(applicationContext, "Failed to registrate", Toast.LENGTH_LONG).show()
        }
    }

    private companion object {
        const val APP_PREFERENCES = "mysettings"
        const val APP_PREFERENCES_TOKEN = "TOKEN"
        //const val APP_PREFERENCES_COOKIE = "COOKIE"
    }
}
