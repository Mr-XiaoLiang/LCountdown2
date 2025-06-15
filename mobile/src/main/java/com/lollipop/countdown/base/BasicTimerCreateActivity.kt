package com.lollipop.countdown.base

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Space
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.lollipop.countdown.R
import com.lollipop.countdown.data.TimeCache
import com.lollipop.countdown.databinding.ActivityTimerCreateBasicBinding
import com.lollipop.countdown.databinding.ItemTimeSpareBinding
import com.lollipop.countdown.helper.TimerManager
import java.util.Calendar
import kotlin.collections.forEach

abstract class BasicTimerCreateActivity : BasicActivity() {

    private val binding by lazy {
        ActivityTimerCreateBasicBinding.inflate(layoutInflater)
    }

    protected val timeList = ArrayList<ItemInfo>()

    protected var currentTime = 0L

    private val itemAdapter by lazy {
        TimeSpareAdapter(timeList, ::onTimeClick)
    }

    protected val calendar: Calendar by lazy {
        Calendar.getInstance()
    }

    protected abstract val doneButtonIcon: Int

    protected val timerManager by lazy {
        TimerManager.Companion.with(this)
    }

    override fun onCreateContentView(): View {
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        updateCurrentTime(System.currentTimeMillis())
        binding.spareTimeList.layoutManager = LinearLayoutManager(
            this, RecyclerView.VERTICAL, false
        )
        binding.spareTimeList.adapter = itemAdapter
        binding.doneButton.setOnClickListener {
            onDoneButtonClick()
            onBackPressedDispatcher.onBackPressed()
        }
        binding.dateInputView.setOnClickListener {
            showDatePicker()
        }
        binding.timeInputView.setOnClickListener {
            showTimePicker()
        }
        binding.doneButton.setIconResource(doneButtonIcon)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        timeList.clear()
        timeList.add(ItemInfo.Space)
        getSpareList().forEach {
            timeList.add(ItemInfo.Time(it))
        }
        timeList.add(ItemInfo.Space)
        itemAdapter.notifyDataSetChanged()
    }

    protected abstract fun getSpareList(): List<TimeCache.TimeInfo>

    protected abstract fun onDoneButtonClick()

    private fun onTimeClick(timeInfo: TimeCache.TimeInfo) {
        updateCurrentTime(timeInfo.time)
    }

    private fun showDatePicker() {
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setSelection(currentTime)
            .setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR)
            .build()
        datePicker.addOnPositiveButtonClickListener { selection ->
            if (selection != null) {
                // 需要计算一下，否则的话，输出的 selection 不会包含时分秒
                updateCurrentTimeOnlyDate(selection)
            }
        }
        datePicker.show(supportFragmentManager, "date_picker")
    }

    private fun showTimePicker() {
        calendar.timeInMillis = currentTime
        val timePicker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .setHour(calendar.get(Calendar.HOUR_OF_DAY))
            .setMinute(calendar.get(Calendar.MINUTE))
            .build()
        timePicker.addOnPositiveButtonClickListener {
            updateCurrentTime(timePicker.hour, timePicker.minute)
        }
        timePicker.show(supportFragmentManager, "time_picker")
    }

    private fun updateCurrentTime(hourOfDay: Int, minute: Int) {
        calendar.timeInMillis = currentTime
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar.set(Calendar.MINUTE, minute)
        updateCurrentTime(calendar.timeInMillis)
    }

    private fun updateCurrentTimeOnlyDate(time: Long) {
        // 需要计算一下，time 中不包含时分秒，我们只提取其中的年月日
        calendar.timeInMillis = time
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        calendar.timeInMillis = currentTime
        calendar.set(year, month, day)
        updateCurrentTime(calendar.timeInMillis)
    }

    private fun updateCurrentTime(time: Long) {
        this.currentTime = time
        updateTimeView()
    }

    private fun updateTimeView() {
        calendar.timeInMillis = currentTime
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        val second = calendar.get(Calendar.SECOND)
        val millis = calendar.get(Calendar.MILLISECOND)
        val dateBuilder = StringBuilder()
        dateBuilder.append(year).append("-")
        if (month < 10) {
            dateBuilder.append("0")
        }
        dateBuilder.append(month + 1).append("-")
        if (day < 10) {
            dateBuilder.append("0")
        }
        dateBuilder.append(day)
        binding.dateInputView.text = dateBuilder.toString()

        val timeBuilder = StringBuilder()
        if (hour < 10) {
            timeBuilder.append("0")
        }
        timeBuilder.append(hour).append(":")
        if (minute < 10) {
            timeBuilder.append("0")
        }
        timeBuilder.append(minute).append(":")
        if (second < 10) {
            timeBuilder.append("0")
        }
        timeBuilder.append(second).append(".")
        if (millis < 100) {
            timeBuilder.append("0")
        }
        if (millis < 10) {
            timeBuilder.append("0")
        }
        timeBuilder.append(millis)

        binding.timeInputView.text = timeBuilder.toString()
    }

    private class TimeSpareAdapter(
        private val timeList: List<ItemInfo>,
        private val onItemClick: (TimeCache.TimeInfo) -> Unit
    ) : RecyclerView.Adapter<ItemHolder>() {

        companion object {
            private const val TYPE_SPACE = 0
            private const val TYPE_TIME = 1
        }

        private var layoutInflater: LayoutInflater? = null

        private fun getLayoutInflater(parent: ViewGroup): LayoutInflater {
            return layoutInflater ?: LayoutInflater.from(parent.context).also {
                layoutInflater = it
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
            when (viewType) {
                TYPE_TIME -> {
                    return ItemHolder.Time(
                        ItemTimeSpareBinding.inflate(
                            getLayoutInflater(parent),
                            parent,
                            false
                        ),
                        ::onItemClick
                    )
                }

                else -> {
                    return ItemHolder.Empty.create(parent, 144)
                }
            }
        }

        private fun onItemClick(position: Int) {
            if (position < 0 || position >= timeList.size) {
                return
            }
            val item = timeList[position]
            if (item is ItemInfo.Time) {
                onItemClick(item.timeInfo)
            }
        }

        override fun onBindViewHolder(holder: ItemHolder, position: Int) {
            when (holder) {
                is ItemHolder.Time -> {
                    val item = timeList[position]
                    if (item is ItemInfo.Time) {
                        holder.bind(item.timeInfo)
                    }
                }

                else -> {

                }
            }
        }

        override fun getItemCount(): Int {
            return timeList.size
        }

        override fun getItemViewType(position: Int): Int {
            return when (timeList[position]) {
                is ItemInfo.Space -> TYPE_SPACE
                is ItemInfo.Time -> TYPE_TIME
            }
        }

    }

    protected sealed class ItemInfo {

        object Space : ItemInfo()

        class Time(
            val timeInfo: TimeCache.TimeInfo
        ) : ItemInfo()
    }

    private sealed class ItemHolder(
        item: View
    ) : RecyclerView.ViewHolder(item) {

        class Empty(
            view: View
        ) : ItemHolder(view) {

            companion object {
                fun create(parent: ViewGroup, heightDp: Int): Empty {
                    val context = parent.context
                    val view = Space(context)
                    view.layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP,
                            heightDp.toFloat(),
                            context.resources.displayMetrics
                        ).toInt()
                    )
                    return Empty(view)
                }
            }
        }

        class Time(
            val binding: ItemTimeSpareBinding,
            val onItemClick: (Int) -> Unit
        ) : ItemHolder(binding.root) {

            init {
                binding.timeView.setOnClickListener {
                    onClick()
                }
            }

            private fun onClick() {
                onItemClick(bindingAdapterPosition)
            }

            fun bind(timeInfo: TimeCache.TimeInfo) {
                binding.timeView.text = timeInfo.display
                when (timeInfo) {
                    is TimeCache.TimeInfo.History -> {
                        binding.itemLabel.isVisible = false
                    }

                    is TimeCache.TimeInfo.ClipBoard -> {
                        binding.itemLabel.isVisible = true
                        binding.itemLabel.setText(R.string.clipboard)
                    }
                }
            }

        }

    }

}