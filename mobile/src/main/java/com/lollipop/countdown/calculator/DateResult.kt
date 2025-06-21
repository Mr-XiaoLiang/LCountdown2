package com.lollipop.countdown.calculator

sealed class DateResult {

    companion object {
        /**
         * 默认的开始时间
         * 0年0月0日0时0分0秒0毫秒
         */
        val START_TIME = Time(-62167507200000)

    }

    class Time(val value: Long) : DateResult()
    class Duration(val value: Long) : DateResult()

    object None : DateResult()

}

fun interface PreviewCallback {
    fun onPreview(result: DateResult?)
}
