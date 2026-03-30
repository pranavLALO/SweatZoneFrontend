package com.example.sweatzone

import android.app.Application
import com.example.sweatzone.data.local.TokenManager

class SweatZoneApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        TokenManager.init(this)
    }
}
