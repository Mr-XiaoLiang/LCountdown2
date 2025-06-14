package com.lollipop.countdown.page

import com.lollipop.countdown.R
import com.lollipop.countdown.base.BasicTimerCreateActivity
import com.lollipop.countdown.data.TimeCache

class TimerCreateActivity : BasicTimerCreateActivity() {

    override val doneButtonIcon: Int = R.drawable.baseline_timer_24px

    override fun getSpareList(): List<TimeCache.TimeInfo> {
        return TimeCache.fetchCache()
    }

    override fun onDoneButtonClick() {
        timerManager.addTimer(currentTime)
    }

}