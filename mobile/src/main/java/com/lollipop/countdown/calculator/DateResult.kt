package com.lollipop.countdown.calculator

sealed class DateResult {

    class Time(val value: Long)
    class Duration(val value: Long)

}

fun interface PreviewCallback {
    fun onPreview(result: DateResult?)
}
