package com.lollipop.countdown

import android.app.Application
import com.lollipop.countdown.data.TimeCache

class LApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        TimeCache.register(this)
    }

}