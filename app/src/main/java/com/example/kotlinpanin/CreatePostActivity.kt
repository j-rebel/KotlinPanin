package com.example.kotlinpanin

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import io.ktor.client.features.ClientRequestException
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.header
import io.ktor.http.Parameters
import io.ktor.util.KtorExperimentalAPI
import kotlinx.android.synthetic.main.activity_create_post.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class CreatePostActivity : AppCompatActivity(), CoroutineScope by MainScope() {

    var TOKEN = ""
    var COOKIE = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)

        val mSettings: SharedPreferences = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
        TOKEN = mSettings.getString(APP_PREFERENCES_TOKEN,"").toString()
        Log.d("Token", TOKEN)
        COOKIE = mSettings.getString(APP_PREFERENCES_COOKIE,"").toString()
        Log.d("Cookie", COOKIE)

        val types: Array<String> = resources.getStringArray(R.array.post_types)
        // access the spinner

        val adapter = ArrayAdapter(this,
                android.R.layout.simple_spinner_item, types)
        typeSpinner.adapter = adapter

        btnAdd.setOnClickListener {
            Log.i("params-type", typeSpinner.selectedItem.toString())
            Log.i("params-text", editText.text.toString())
            Log.i("params-video", editVideo.text.toString())
            Log.i("params-address", editAddress.text.toString())
            Log.i("params-long", editLong.text.toString())
            Log.i("params-lat", editLat.text.toString())
            addPost(typeSpinner.selectedItem.toString(),
                    editText.text.toString(),
                    editVideo.text.toString(),
                    editAddress.text.toString(),
                    editLong.text.toString(),
                    editLat.text.toString()
                    )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("CreatePostActivity", "onDestroy")
        cancel()
        finish()
    }

    override fun onStop() {
        super.onStop()
        Log.d("CreatePostActivity", "onStop")
        cancel()
        finish()
    }

    override fun onPause() {
        super.onPause()
        Log.d("CreatePostActivity", "onPause")
        cancel()
    }

    @KtorExperimentalAPI
    fun addPost(type: String, text: String, video: String, address: String, geoLong: String, geoLat: String) = launch {
        try {
            val params = Parameters.build {
                append("type", type)
                append("repost", "0")
                append("text", text)
                append("video", video)
                append("address", address)
                append("geo_long", geoLong)
                append("geo_lat", geoLat)
            }
            Api.client.submitForm(Api.postUrl, params, false) {
                header("Authorization", "Bearer $TOKEN")
                header("Cookie", COOKIE)
            }
        } catch (e: ClientRequestException) {
            Log.e("Error", e.message)
        }
    }

    private companion object {
        const val APP_PREFERENCES = "mysettings"
        const val APP_PREFERENCES_TOKEN = "TOKEN"
        const val APP_PREFERENCES_COOKIE = "COOKIE"
    }
}
