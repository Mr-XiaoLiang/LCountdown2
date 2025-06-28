package com.lollipop.countdown.calculator

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.util.Log
import android.view.MenuItem
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import com.lollipop.countdown.R
import com.lollipop.countdown.calculator.abacus.DateAbacus


class PreviewDelegate(private val previewView: TextView) : PopupMenu.OnMenuItemClickListener {

    private var result: DateResult? = null

    private val labelCache by lazy {
        LabelCache()
    }

    private val format by lazy {
        TimeFormat.Millisecond
    }

    private val context by lazy {
        previewView.context
    }

    init {
        previewView.setOnClickListener { onViewClick() }
    }

    fun previewMode(onlyPreview: Boolean) {
        previewView.alpha = if (onlyPreview) {
            0.5f
        } else {
            1f
        }
    }

    private fun onViewClick() {
        val r = result ?: return
        if (r is DateResult.None) {
            return
        }
        PopupMenu(context, previewView).also {
            it.setOnMenuItemClickListener(this)
            it.inflate(R.menu.menu_date_calculator_result)
            it.show()
        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.copyTimeStamp -> {
                result?.let { r ->
                    copyTimeStamp(r)
                }
            }

            R.id.copyTimeValue -> {
                result?.let { r ->
                    copyDateValue(r)
                }
            }

            else -> {
                return false
            }
        }
        return true
    }

    private fun copyDateValue(r: DateResult) {
        when (r) {
            is DateResult.Time -> {
                copyValue(getByTime(r.value))
            }

            is DateResult.Duration -> {
                copyValue(getByDuration(r.value))
            }

            DateResult.None -> {
            }
        }
    }

    private fun copyTimeStamp(r: DateResult) {
        when (r) {
            is DateResult.Time -> {
                copyValue(r.value.toString())
            }

            is DateResult.Duration -> {
                copyValue(r.value.toString())
            }

            DateResult.None -> {
            }
        }
    }

    fun update(result: DateResult?) {
        this.result = result
        updateView()
    }

    private fun updateView() {
        val result = result
        if (result == null) {
            previewView.text = ""
            return
        }
        when (result) {
            is DateResult.Time -> {
                previewView.text = getByTime(result.value)
            }

            is DateResult.Duration -> {
                previewView.text = getByDuration(result.value)
            }

            DateResult.None -> {
                previewView.text = ""
            }
        }
    }

    private fun getByTime(time: Long): String {
        return format.format(time)
    }

    private fun getByDuration(time: Long): String {
        val builder = StringBuilder()
        var value = time
        if (time < 0) {
            builder.append("-")
            value = -value
        }
        while (value > 0L) {
            when {
                value >= DateAbacus.ONE_YEAR -> {
                    val year = value / DateAbacus.ONE_YEAR
                    value %= DateAbacus.ONE_YEAR
                    builder.append(year)
                        .append(labelCache.getLabel(context, R.string.label_year))
                }

                value >= DateAbacus.ONE_MONTH -> {
                    val month = value / DateAbacus.ONE_MONTH
                    value %= DateAbacus.ONE_MONTH
                    builder.append(month)
                        .append(labelCache.getLabel(context, R.string.label_month))
                }

                value >= DateAbacus.ONE_DAY -> {
                    val day = value / DateAbacus.ONE_DAY
                    value %= DateAbacus.ONE_DAY
                    builder.append(day)
                        .append(labelCache.getLabel(context, R.string.label_day))
                }

                value >= DateAbacus.ONE_HOUR -> {
                    val hour = value / DateAbacus.ONE_HOUR
                    value %= DateAbacus.ONE_HOUR
                    builder.append(hour)
                        .append(labelCache.getLabel(context, R.string.label_hour))
                }

                value >= DateAbacus.ONE_MINUTE -> {
                    val minute = value / DateAbacus.ONE_MINUTE
                    value %= DateAbacus.ONE_MINUTE
                    builder.append(minute)
                        .append(labelCache.getLabel(context, R.string.label_minute))
                }

                value >= DateAbacus.ONE_SECOND -> {
                    val second = value / DateAbacus.ONE_SECOND
                    value %= DateAbacus.ONE_SECOND
                    builder.append(second)
                        .append(labelCache.getLabel(context, R.string.label_second))
                }

                else -> {
                    val millisecond = value
                    // 用完要清零
                    value = 0L
                    builder.append(millisecond)
                    Log.e("Lollipop", "getByDuration: ${builder.length}")
                    builder.append(labelCache.getLabel(context, R.string.label_millisecond))
                }
            }
        }
        return builder.toString()
    }

    private fun copyValue(value: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) ?: return
        if (clipboard is ClipboardManager) {
            val clip = ClipData.newPlainText(value, value)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(context, R.string.copied, Toast.LENGTH_SHORT).show()
        }
    }

    private class LabelCache {

        private val labelMap = mutableMapOf<Int, String>()

        fun getLabel(context: Context, field: Int): String {
            val string = labelMap[field]
            if (string != null) {
                return string
            }
            val value = context.getString(field)
            labelMap[field] = value
            return value
        }

    }

}