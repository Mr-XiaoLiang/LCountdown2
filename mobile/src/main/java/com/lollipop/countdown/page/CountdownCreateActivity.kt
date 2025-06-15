package com.lollipop.countdown.page

import com.lollipop.countdown.R
import com.lollipop.countdown.base.BasicTimerCreateActivity
import com.lollipop.countdown.data.TimeCache

class CountdownCreateActivity : BasicTimerCreateActivity() {

    override val doneButtonIcon: Int = R.drawable.baseline_hourglass_24px

    override fun getSpareList(): List<TimeCache.TimeInfo> {
        return TimeCache.clipboardTime
    }

    override fun onDoneButtonClick() {
        timerManager.addCountdown(currentTime)
    }

}