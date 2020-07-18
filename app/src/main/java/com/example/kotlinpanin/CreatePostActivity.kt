package com.example.kotlinpanin

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import io.ktor.client.features.ClientRequestException
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.header
import io.ktor.client.statement.HttpResponse
import io.ktor.http.Parameters
import io.ktor.util.KtorExperimentalAPI
import kotlinx.android.synthetic.main.activity_create_post.*
import kotlinx.android.synthetic.main.activity_create_post.view.*
import kotlinx.coroutines.launch

class CreatePostActivity : AppCompatActivity() {

    var TOKEN = ""

    @KtorExperimentalAPI
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
        typeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
            ) {
                if (position == 0 || position == 2) {
                    editVideo.isVisible = false
                    editAddress.isVisible = false
                    editLat.isVisible = false
                    editLong.isVisible = false
                } else if (position == 1) {
                    editVideo.isVisible = true
                    editAddress.isVisible = false
                    editLat.isVisible = false
                    editLong.isVisible = false
                } else if (position == 3) {
                    editVideo.isVisible = false
                    editAddress.isVisible = true
                    editLat.isVisible = true
                    editLong.isVisible = true
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                editVideo.isVisible = false
                editAddress.isVisible = false
                editLat.isVisible = false
                editLong.isVisible = false
            }
        }

        btnAdd.setOnClickListener {
            val type = if (typeSpinner.selectedItem.toString().equals("")) "TEXT" else typeSpinner.selectedItem.toString()
            val text = if (editText.text.toString().equals("")) "default" else editText.text.toString()
            val video = if (editVideo.text.toString().equals("")) "default" else editVideo.text.toString()
            val address = if (editAddress.text.toString().equals("")) "default" else editAddress.text.toString()
            val long = if (editLong.text.toString().equals("")) "0" else editLong.text.toString()
            val lat = if (editLat.text.toString().equals("")) "0" else editLat.text.toString()

            addPost(type,
                    text,
                    video,
                    address,
                    long,
                    lat
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