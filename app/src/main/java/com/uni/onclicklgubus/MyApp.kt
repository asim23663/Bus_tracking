package com.uni.onclicklgubus

import android.app.Application
import android.content.Context

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {

        @JvmStatic
        var instance: MyApp? = null
            private set

        @JvmStatic
        val context: Context
            get() = instance!!.applicationContext
    }
}