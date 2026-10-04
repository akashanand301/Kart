package com.example.kart

import android.app.Application
import com.example.kart.core.network.AppContainer
import com.example.kart.core.network.DefaultAppContainer

class KartApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
    }
}
