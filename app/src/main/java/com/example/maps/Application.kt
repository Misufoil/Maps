package com.example.maps

import android.app.Application
import com.yandex.mapkit.MapKitFactory

class Application: Application() {
    override fun onCreate() {
        super.onCreate()
        MapKitFactory.setApiKey("8a475a2a-212a-4d71-8358-10b7b5b0d929")
    }
}