package com.example.kotlinpanin

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import androidx.lifecycle.lifecycleScope
import io.ktor.client.features.ClientRequestException
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.header
import io.ktor.client.statement.HttpResponse
import io.ktor.http.Parameters
import io.ktor.util.KtorExperimentalAPI
import kotlinx.android.synthetic.main.activity_create_post.*
import kotlinx.coroutines.launch

class CreatePostActivity : AppCompatActivity() {

    var TOKEN = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)

        val mSettings: SharedPreferences = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
        TOKEN = mSettings.getString(APP_PREFERENCES_TOKEN,"").toString()
        Log.d("Token", TOKEN)

        val types: Array<String> = resources.getStringArray(R.array.post_types)

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

    @KtorExperimentalAPI
    fun addPost(type: String, text: String, video: String, address: String, geoLong: String, geoLat: String) = lifecycleScope.launch {
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
            Api.client.submitForm<HttpResponse>(Api.postUrl, params, false) {
                header("Authorization", "Bearer $TOKEN")
            }
            finish()
        } catch (e: ClientRequestException) {
            Log.e("Error", e.message)
        }
    }

    private companion object {
        const val APP_PREFERENCES = "mysettings"
        const val APP_PREFERENCES_TOKEN = "TOKEN"
    }
}