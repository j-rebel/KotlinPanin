package com.example.kotlinpanin

import android.app.Application
import android.content.Context
import net.danlew.android.joda.JodaTimeAndroid
import org.joda.time.DateTimeZone

class App: Application() {

    init {
        instance = this
    }

    companion object {
        private var instance: App? = null

        fun applicationContext() : Context {
            return instance!!.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        JodaTimeAndroid.init(this)
        DateTimeZone.setDefault(DateTimeZone.UTC)
        val context: Context = App.applicationContext()
    }


}