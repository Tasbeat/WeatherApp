package com.example.myapplication

import android.app.Application
import uk.co.chrisjenx.calligraphy.CalligraphyConfig


class ConfigApplication : Application () {
    override fun onCreate() {
        super.onCreate()
        CalligraphyConfig.initDefault(
            CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/b_nazanin.ttf")
                .build()
        )
    }
}
