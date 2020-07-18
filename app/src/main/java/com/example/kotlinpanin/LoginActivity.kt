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

class LoginActivity : AppCompatActivity() {

    lateinit var mSettings: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("LoginActivity", "onCreate")
        setContentView(R.layout.activity_login)

        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
        checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
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

    fun login(email: String, password: String) = lifecycleScope.launch {
        btnLogin.isVisible = false
        emailInput.isEnabled = false
        emailInput.isClickable = false
        passInput.isEnabled = false
        passInput.isClickable = false
        displayNameInput.isEnabled = false
        displayNameInput.isClickable = false
        avatarInput.isEnabled = false
        avatarInput.isClickable = false
        checkBox.isEnabled = false
        checkBox.isClickable = false
        pb.isVisible = true
        try {
            val params = Parameters.build {
                append("email", email)
                append("password", password)
            }
            val requestedToken = Api.client.submitForm<Token>(Api.loginUrl, params, false)
            mSettings.edit {
                putString(APP_PREFERENCES_TOKEN, requestedToken.token)
            }
            val intent = Intent(App.applicationContext(), MainActivity::class.java)
            startActivity(intent)
            finish()
        } catch (e: Exception) {
            Log.e("Login", e.message, Throwable())
        } finally {
            pb.isVisible = false
            btnLogin.isVisible = true
            emailInput.isEnabled = true
            emailInput.isClickable = true
            passInput.isEnabled = true
            passInput.isClickable = true
            displayNameInput.isEnabled = true
            displayNameInput.isClickable = true
            avatarInput.isEnabled = true
            avatarInput.isClickable = true
            checkBox.isEnabled = true
            checkBox.isClickable = true
        }
    }

    fun registrate(email: String, password: String, displayName: String, avatar: String) = lifecycleScope.launch {
        btnRegestrate.isVisible = false
        emailInput.isEnabled = false
        emailInput.isClickable = false
        passInput.isEnabled = false
        passInput.isClickable = false
        displayNameInput.isEnabled = false
        displayNameInput.isClickable = false
        avatarInput.isEnabled = false
        avatarInput.isClickable = false
        pb.isVisible = true
        try {
            val params = Parameters.build {
                append("displayName", displayName)
                append("email", email)
                append("password", password)
                append("avatar", avatar)
            }
            Api.client.submitForm<Token>(Api.registrationUrl, params, false)
            login(email, password)
        } catch (e: Exception) {
            Log.e("Registration", e.message, Throwable())
            Toast.makeText(applicationContext, "Failed to registrate", Toast.LENGTH_LONG).show()
        }
    }

    private companion object {
        const val APP_PREFERENCES = "mysettings"
        const val APP_PREFERENCES_TOKEN = "TOKEN"
    }
}
