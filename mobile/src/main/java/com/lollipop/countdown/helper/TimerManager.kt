package com.lollipop.countdown.helper

import android.content.Context
import android.widget.Toast

class TimerManager(private val context: Context) {

    companion object {
        fun with(context: Context): TimerManager {
            return TimerManager(context)
        }
    }

    fun addTimer(time: Long) {
        // TODO
        Toast.makeText(context, "TimerManager.addTimer(${time.toString(16)})", Toast.LENGTH_SHORT)
            .show()
    }

    fun addCountdown(time: Long) {
        // TODO
        Toast.makeText(
            context,
            "TimerManager.addCountdown(${time.toString(16)})",
            Toast.LENGTH_SHORT
        )
            .show()
    }

}