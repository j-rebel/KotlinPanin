package com.example.kotlinpanin

import android.app.Application
import net.danlew.android.joda.JodaTimeAndroid
import org.joda.time.DateTimeZone

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        JodaTimeAndroid.init(this)
        DateTimeZone.setDefault(DateTimeZone.UTC)
    }
}