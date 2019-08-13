package com.twitter.twitterchallenge

import android.app.Application
import android.content.Context

class WeatherApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }
    init {
        instance = this
    }

    companion object {
        private var instance: WeatherApplication? = null

        fun applicationContext() : Context {
            return instance!! as WeatherApplication
        }
    }
}

