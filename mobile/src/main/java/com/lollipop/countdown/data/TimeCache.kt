package com.lollipop.countdown.data

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.lollipop.countdown.calculator.TimeFormat
import java.util.Date
import java.util.concurrent.CopyOnWriteArrayList

object TimeCache {

    private val dateFormat by lazy {
        TimeFormat.DateTime
    }

    private val historyTimeList = CopyOnWriteArrayList<TimeInfo.History>()
    private val clipboardTimeList = CopyOnWriteArrayList<TimeInfo.ClipBoard>()

    private val activityLifecycleCallback = ActivityLifecycleCallback(::onActivityResumed)

    val appTime: List<TimeInfo>
        get() {
            return historyTimeList
        }

    val clipboardTime: List<TimeInfo>
        get() {
            return clipboardTimeList
        }

    fun fetchCache(): List<TimeInfo> {
        return clipboardTime + appTime
    }

    fun formatTime(time: Long): String {
        return dateFormat.format(time)
    }

    private fun onActivityResumed(firstResume: Boolean) {
        if (firstResume) {
            rememberAppTimeNow()
        }
    }

    fun resetClipboardTime(time: Long) {
        if (clipboardTimeList.isNotEmpty()) {
            // 添加到app启动时间列表
            historyTimeList.addAll(0, clipboardTimeList.map { TimeInfo.History(it.time) })
        }
        clipboardTimeList.clear()
        clipboardTimeList.add(TimeInfo.ClipBoard(time))
    }

    fun register(application: Application) {
        // 记录第一次启动的时间
        rememberAppTimeNow()
        // 记录后续的每次前后台切换
        application.registerActivityLifecycleCallbacks(activityLifecycleCallback)
    }

    private fun rememberAppTimeNow() {
        val timeInfo = TimeInfo.History(System.currentTimeMillis())
        historyTimeList.add(0, timeInfo)
    }

    sealed class TimeInfo(
        val time: Long
    ) {

        val display: String by lazy {
            formatTime(time)
        }

        class History(
            time: Long
        ) : TimeInfo(time)

        class ClipBoard(
            time: Long
        ) : TimeInfo(time)

    }

    private class ActivityLifecycleCallback(
        val onResume: (firstResume: Boolean) -> Unit
    ) : Application.ActivityLifecycleCallbacks {

        private var activeActivityCount = 0
        private var isLastActivityStop = true

        override fun onActivityCreated(
            activity: Activity,
            savedInstanceState: Bundle?
        ) {
        }

        override fun onActivityDestroyed(activity: Activity) {
        }

        override fun onActivityPaused(activity: Activity) {
            activeActivityCount--
        }

        override fun onActivityResumed(activity: Activity) {
            val firstResume = activeActivityCount < 1 && isLastActivityStop
            activeActivityCount++
            isLastActivityStop = false
            onResume(firstResume)
        }

        override fun onActivitySaveInstanceState(
            activity: Activity,
            outState: Bundle
        ) {
        }

        override fun onActivityStarted(activity: Activity) {
        }

        override fun onActivityStopped(activity: Activity) {
            if (activeActivityCount < 1) {
                isLastActivityStop = true
            }
        }
    }

}