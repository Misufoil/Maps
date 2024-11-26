package com.example.maps

import android.app.Application
import android.content.Context
import com.yandex.mapkit.MapKitFactory

class MyApplication: Application() {

    val appComponent: AppComponent by lazy {
        DaggerAppComponent.builder()
            .build()
    }


    override fun onCreate() {
        super.onCreate()
        MapKitFactory.setApiKey("8a475a2a-212a-4d71-8358-10b7b5b0d929")
    }

}

val Context.appComponent: AppComponent
    get() = when (this) {
        is MyApplication -> appComponent
        else -> this.applicationContext.appComponent
    }