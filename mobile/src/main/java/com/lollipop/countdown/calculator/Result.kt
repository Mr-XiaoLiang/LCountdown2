package com.lollipop.countdown.calculator

sealed class Result {

    class Time(val value: Long)
    class Duration(val value: Long)

}