package com.lollipop.countdown.data

import android.content.Intent
import android.text.TextUtils
import com.lollipop.countdown.helper.GenerateColorHelper

/**
 * 计时的bean
 * @author Lollipop
 */
class TimingInfo(var id: Int = 0) {

    companion object {
        private const val ID = "TIMING_BEAN_ID"
        private const val NAME = "TIMING_BEAN_NAME"
        private const val COUNTDOWN = "TIMING_BEAN_COUNTDOWN"
        private const val START = "TIMING_BEAN_START"
        private const val END = "TIMING_BEAN_END"
    }

    var name = ""

    var isCountdown = false

    var startTime = 0L

    var endTime = 0L

    fun putTo(intent: Intent) {
        intent.putExtra(ID, id)
        intent.putExtra(NAME, name)
        intent.putExtra(COUNTDOWN, isCountdown)
        intent.putExtra(START, startTime)
        intent.putExtra(END, endTime)
    }

    fun parseFrom(intent: Intent) {
        id = intent.getIntExtra(ID, 0)
        name = intent.getStringExtra(NAME) ?: ""
        isCountdown = intent.getBooleanExtra(COUNTDOWN, false)
        startTime = intent.getLongExtra(START, 0L)
        endTime = intent.getLongExtra(END, 0L)
    }

    private var lastName = name
    private var lastStartTime = startTime
    private var lastColor = getBeanColor()

    private fun getBeanColor(): Int {
        val value = if (TextUtils.isEmpty(name)) {
            startTime.toString(16)
        } else {
            name
        }
        return GenerateColorHelper.generate(value)

    }

    val color: Int
        get() {

            if (name != lastName || lastStartTime != startTime) {
                lastColor = getBeanColor()
            }

            return lastColor
        }

}
