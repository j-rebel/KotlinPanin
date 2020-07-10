package com.example.kotlinpanin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("SplashActivity", "onCreate")
        setContentView(R.layout.activity_splash)
    }

    override fun onDestroy() {
        super.onDestroy()
        finish()
    }

    override fun onStop() {
        super.onStop()
        finish()
    }
}
