package com.lollipop.countdown.calculator

import java.text.SimpleDateFormat
import java.util.Locale

sealed class TimeFormat {

    abstract val formatPattern: String

    private val sdf by lazy {
        SimpleDateFormat(formatPattern, Locale.getDefault())
    }

    fun format(time: Long): String {
        return format(java.util.Date(time))
    }

    fun format(time: java.util.Date): String {
        return sdf.format(time)
    }

    object Date : TimeFormat() {
        override val formatPattern: String = "yyyy-MM-dd"
    }

    object Time : TimeFormat() {
        override val formatPattern: String = "HH:mm:ss"
    }

    object DateTime : TimeFormat() {
        override val formatPattern: String = "yyyy-MM-dd HH:mm:ss"
    }

    object Millisecond : TimeFormat() {
        override val formatPattern: String = "yyyy-MM-dd HH:mm:ss.SSS"
    }

}